### Uygulamanızı izlemek için Docker Compose kullanarak Prometheus ve Grafana'yı kurdunuz. Ancak, Grafana'yı açtığınızda Prometheus'tan gelen metrikleri göremezsiniz. Bu kurulumda Grafana'nın Prometheus'tan gelen metrikleri görüntüleyememesinin olası nedenlerini listeleyin ve bu sorunu çözmek için somut adımlar veya yapılandırma değişiklikleri önerin.

Grafana’nın Prometheus’tan metrikleri görüntüleyememesinin birkaç olası nedeni olabilir. Docker Compose ile Prometheus ve Grafana’yı çalıştırırken, bu sorun genellikle ağ yapılandırması, veri kaynağı ayarları, servis erişilebilirliği veya yanlış konfigürasyonlardan kaynaklanır. Aşağıda, bu sorunun potansiyel nedenlerini ve her biri için çözüm adımlarını Türkçe olarak listeliyorum.

---

### Olası Nedenler ve Çözüm Adımları

#### 1. **Prometheus ve Grafana Aynı Docker Ağı İçinde Değil**
   - **Neden**: Docker Compose’da varsayılan olarak her proje kendi bridge ağını oluşturur, ancak servisler farklı ağlarda çalışıyorsa veya ağ yapılandırması yanlışsa, Grafana Prometheus’a erişemez.
   - **Belirti**: Grafana, Prometheus’un URL’sine (örneğin, `http://prometheus:9090`) bağlanamaz ve bağlantı hatası alır.
   - **Çözüm**:
     - **Kontrol**: `docker-compose.yml` dosyasını kontrol edin ve her iki servisin aynı ağda olduğunu doğrulayın. Varsayılan bridge ağı genellikle yeterlidir, ancak açıkça bir ağ tanımlanmışsa, her iki servisin de bu ağa bağlı olduğundan emin olun.
     - **Yapılandırma Değişikliği**:
       ```yaml
       version: '3.8'
       services:
         prometheus:
           image: prom/prometheus:latest
           ports:
             - "9090:9090"
           volumes:
             - ./prometheus.yml:/etc/prometheus/prometheus.yml
           networks:
             - monitoring
         grafana:
           image: grafana/grafana:latest
           ports:
             - "3000:3000"
           depends_on:
             - prometheus
           networks:
             - monitoring
       networks:
         monitoring:
           driver: bridge
       ```
       - Yukarıdaki yapılandırma, her iki servisin `monitoring` adlı bir ağda çalıştığını garanti eder.
     - **Test**: Grafana konteynerinden Prometheus’a erişimi test etmek için:
       ```bash
       docker exec -it grafana-container curl http://prometheus:9090
       ```
       Eğer yanıt alınıyorsa, ağ bağlantısı çalışıyor demektir.

#### 2. **Grafana’da Yanlış Prometheus Veri Kaynağı Ayarları**
   - **Neden**: Grafana’da Prometheus veri kaynağı yanlış bir URL, port veya kimlik doğrulama ayarlarıyla yapılandırılmış olabilir.
   - **Belirti**: Grafana’da veri kaynağı eklenirken “Connection failed” veya “No data” hatası alınır.
   - **Çözüm**:
     - **Kontrol**: Grafana’nın web arayüzünde Prometheus veri kaynağını kontrol edin (Configuration > Data Sources > Prometheus).
     - **Yapılandırma Değişikliği**:
       - Grafana’da veri kaynağı eklerken, Prometheus’un URL’sini `http://prometheus:9090` olarak ayarlayın (Docker Compose’da servis adı `prometheus` ise).
       - Kimlik doğrulama kullanıyorsanız, doğru kullanıcı adı ve şifreyi girin.
       - Örnek Grafana veri kaynağı ayarları:
         - **URL**: `http://prometheus:9090`
         - **Access**: Server (default)
         - **Auth**: Gerekirse kimlik doğrulama ayarlarını kapatın veya doğru bilgileri girin.
     - **Test**: Grafana’da “Save & Test” butonuna tıklayın. “Data source is working” mesajı almalısınız.

#### 3. **Prometheus’un Yanlış veya Eksik Konfigürasyonu**
   - **Neden**: Prometheus’un `prometheus.yml` dosyasında hedef (target) metrik uç noktaları doğru şekilde tanımlanmamış olabilir veya metrikler hiç toplanmıyor olabilir.
   - **Belirti**: Prometheus’un kendi arayüzünde (`http://localhost:9090`) metrikler görünmüyor veya hedefler “DOWN” durumunda.
   - **Çözüm**:
     - **Kontrol**: `prometheus.yml` dosyasını inceleyin. Örneğin, uygulamanızın metriklerini toplayan bir uç nokta (endpoint) tanımlı olmalı:
       ```yaml
       global:
         scrape_interval: 15s
       scrape_configs:
         - job_name: 'my-app'
           static_configs:
             - targets: ['app:5000'] # Uygulamanızın metrik uç noktası
       ```
       - Yukarıda, uygulamanızın `app` adında bir servis olduğunu ve metrikleri `5000` portunda sunduğunu varsayıyorum.
     - **Yapılandırma Değişikliği**:
       - Uygulamanızın metrik uç noktasını (örneğin, `/metrics`) Prometheus’un scrape etmesi için doğru hedefi ekleyin.
       - `docker-compose.yml` dosyasında Prometheus’un bu konfigürasyonu okuduğundan emin olun:
         ```yaml
         volumes:
           - ./prometheus.yml:/etc/prometheus/prometheus.yml
         ```
     - **Test**: Prometheus’un web arayüzüne gidin (`http://localhost:9090/targets`) ve hedeflerin “UP” durumunda olduğunu kontrol edin.

#### 4. **Prometheus Servisinin Çalışmaması veya Erişilemez Olması**
   - **Neden**: Prometheus konteyneri çalışmıyor, çökmüş veya yanlış bir portta çalışıyor olabilir.
   - **Belirti**: `docker ps` komutunda Prometheus konteyneri görünmüyor veya Grafana bağlantı hatası alıyor.
   - **Çözüm**:
     - **Kontrol**: Prometheus’un çalıştığını doğrulayın:
       ```bash
       docker ps
       ```
       Eğer Prometheus konteyneri listelenmiyorsa, logları kontrol edin:
       ```bash
       docker logs prometheus-container
       ```
     - **Yapılandırma Değişikliği**:
       - `docker-compose.yml` dosyasında Prometheus’un portlarını ve konfigürasyonunu kontrol edin:
         ```yaml
         prometheus:
           image: prom/prometheus:latest
           ports:
             - "9090:9090"
           volumes:
             - ./prometheus.yml:/etc/prometheus/prometheus.yml
         ```
       - Prometheus’un konfigürasyon dosyasını doğru okuduğundan emin olun.
     - **Test**: Prometheus’un arayüzüne erişin (`http://localhost:9090`) ve metriklerin göründüğünü kontrol edin.

#### 5. **Uygulamanın Metrikleri Sunmaması**
   - **Neden**: Uygulamanız metrikleri (örneğin, `/metrics` endpoint’i) sunmuyorsa, Prometheus veri toplayamaz ve Grafana’da metrikler görünmez.
   - **Belirti**: Prometheus’ta hedef “UP” olsa bile metrikler boş.
   - **Çözüm**:
     - **Kontrol**: Uygulamanızın Prometheus metriklerini sunduğunu doğrulayın. Örneğin, bir Python uygulamasında `prometheus_client` kütüphanesi kullanılabilir:
       ```python
       from prometheus_client import start_http_server, Counter
       import time

       c = Counter('requests_total', 'Total requests')
       start_http_server(5000)
       while True:
           c.inc()
           time.sleep(1)
       ```
     - **Yapılandırma Değişikliği**:
       - Uygulamanıza bir metrik endpoint’i ekleyin (örneğin, `/metrics`).
       - `docker-compose.yml` dosyasında uygulamanın bu portu sunduğunu doğrulayın:
         ```yaml
         app:
           image: python:3.9
           volumes:
             - ./app:/app
           working_dir: /app
           command: python app.py
           expose:
             - "5000"
         ```
     - **Test**: Uygulamanın metrik endpoint’ine erişin:
       ```bash
       curl http://localhost:5000/metrics
       ```

#### 6. **Grafana’da Yanlış Sorgular (Queries)**
   - **Neden**: Grafana’da oluşturulan panellerde yanlış PromQL sorguları kullanılmış olabilir.
   - **Belirti**: Veri kaynağı çalışıyor, ancak panellerde “No data” görünüyor.
   - **Çözüm**:
     - **Kontrol**: Grafana’da panel sorgularını kontrol edin. Örneğin, `requests_total` metrikini görüntülemek için:
       ```
       rate(requests_total[5m])
       ```
     - **Yapılandırma Değişikliği**:
       - Doğru metrik adlarını ve PromQL sorgularını kullanın. Prometheus’un arayüzünde (`http://localhost:9090`) metrik adlarını kontrol edin.
     - **Test**: Grafana’da bir panel oluşturun ve basit bir sorgu (örneğin, `up`) ile metriklerin göründüğünü doğrulayın.

#### 7. **Firewall veya Güvenlik Ayarları**
   - **Neden**: Docker host’unda veya ağda bir firewall, Prometheus’un 9090 portuna veya uygulamanın metrik portuna erişimi engelliyor olabilir.
   - **Belirti**: Bağlantı zaman aşımı (timeout) veya erişim engellendi hataları.
   - **Çözüm**:
     - **Kontrol**: Docker host’unda firewall kurallarını kontrol edin:
       ```bash
       sudo ufw status
       ```
     - **Yapılandırma Değişikliği**:
       - Gerekli portları açın:
         ```bash
         sudo ufw allow 9090
         sudo ufw allow 3000
         ```
     - **Test**: Prometheus ve Grafana’ya tarayıcıdan veya `curl` ile erişmeyi deneyin.

#### 8. **Zaman Senkronizasyonu Sorunları**
   - **Neden**: Prometheus ve Grafana konteynerlerinin saatleri senkronize değilse, metrikler yanlış zaman diliminde görünebilir veya hiç görünmeyebilir.
   - **Belirti**: Grafana’da metrikler var, ancak yanlış zaman aralığında görünüyor.
   - **Çözüm**:
     - **Kontrol**: Konteynerlerin saat dilimlerini kontrol edin:
       ```bash
       docker exec prometheus-container date
       docker exec grafana-container date
       ```
     - **Yapılandırma Değişikliği**:
       - `docker-compose.yml` dosyasında saat dilimini ayarlayın:
         ```yaml
         prometheus:
           image: prom/prometheus:latest
           environment:
             - TZ=Europe/Istanbul
         grafana:
           image: grafana/grafana:latest
           environment:
             - TZ=Europe/Istanbul
         ```
     - **Test**: Grafana’da zaman aralığını kontrol edin ve “Last 5 minutes” gibi bir aralık seçerek metrikleri görüntüleyin.

---

### Örnek Docker Compose Dosyası (Tamamlanmış)

```yaml
version: '3.8'
services:
  prometheus:
    image: prom/prometheus:latest
    ports:
      - "9090:9090"
    volumes:
      - ./prometheus.yml:/etc/prometheus/prometheus.yml
    networks:
      - monitoring
  grafana:
    image: grafana/grafana:latest
    ports:
      - "3000:3000"
    depends_on:
      - prometheus
    environment:
      - GF_SECURITY_ADMIN_USER=admin
      - GF_SECURITY_ADMIN_PASSWORD=admin
    networks:
      - monitoring
  app:
    image: python:3.9
    volumes:
      - ./app:/app
    working_dir: /app
    command: python app.py
    expose:
      - "5000"
    networks:
      - monitoring
networks:
  monitoring:
    driver: bridge
```

**Prometheus Konfigürasyonu (`prometheus.yml`)**:
```yaml
global:
  scrape_interval: 15s
scrape_configs:
  - job_name: 'app'
    static_configs:
      - targets: ['app:5000']
```

---

### Genel Çözüm Adımları
1. **Ağ Kontrolü**: `docker network ls` ve `docker network inspect <ağ_adı>` ile servislerin aynı ağda olduğunu doğrulayın.
2. **Servis Sağlığı**: `docker ps` ve `docker logs` ile Prometheus ve Grafana’nın çalıştığını kontrol edin.
3. **Veri Kaynağı Testi**: Grafana’da Prometheus veri kaynağını `http://prometheus:9090` ile test edin.
4. **Metrik Kontrolü**: Prometheus’un arayüzünde (`http://localhost:9090`) metriklerin toplandığını doğrulayın.
5. **Sorgu Kontrolü**: Grafana’da doğru PromQL sorgularını kullanın.

---

### Özet
Grafana’nın Prometheus’tan metrikleri görüntüleyememesinin yaygın nedenleri arasında ağ sorunları, yanlış veri kaynağı ayarları, Prometheus’un konfigürasyon hataları, servislerin çalışmaması veya uygulamanın metrik sunmaması yer alır. Yukarıdaki adımları takip ederek bu sorunları teşhis edebilir ve çözebilirsiniz. Örnek `docker-compose.yml` ve `prometheus.yml` dosyaları, sorunsuz bir izleme sistemi kurmanıza yardımcı olacaktır. Eğer sorun devam ederse, logları (`docker logs`) ve Grafana’nın hata mesajlarını paylaşarak daha spesifik bir teşhis yapılabilir.
