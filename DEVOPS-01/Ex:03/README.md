### Docker Compose nedir ve başlıca kullanım alanları nelerdir?
### Docker Compose Nedir?

**Docker Compose**, birden fazla Docker konteynerini tanımlamak, yapılandırmak ve çalıştırmak için kullanılan bir araçtır. YAML formatında bir konfigürasyon dosyası (`docker-compose.yml`) kullanılarak, birden fazla konteynerin (örneğin, bir web uygulaması ve veritabanı) nasıl çalışacağı, ağ ayarları, bağımlılıklar ve ortam değişkenleri gibi detaylar tanımlanır. Docker Compose, özellikle geliştirme, test ve yerel ortamlar için çoklu konteyner uygulamalarını kolayca yönetmek için tasarlanmıştır.

Docker Compose, Docker CLI'nin bir parçasıdır ve Docker ile birlikte gelir. Tek bir komutla (örneğin, `docker-compose up`) tüm konteynerleri başlatabilir, durdurabilir veya yeniden yapılandırabilirsiniz.

**Temel Özellikleri:**
- **Deklaratif Yapılandırma**: YAML dosyasıyla konteynerlerin nasıl çalışacağı tanımlanır.
- **Otomasyon**: Birden fazla konteyneri tek bir komutla başlatır ve yönetir.
- **Bağımlılık Yönetimi**: Konteynerler arasındaki bağımlılıkları (örneğin, bir uygulamanın veritabanına bağlı olması) otomatik olarak yönetir.
- **Ağ ve Hacim Desteği**: Konteynerler arasında ağ oluşturur ve veri kalıcılığı için hacimleri (volumes) yapılandırır.
- **Taşınabilirlik**: YAML dosyası, farklı ortamlarda aynı yapılandırmayı kullanmak için taşınabilir.

---

### Docker Compose'un Ana Kullanım Alanları

1. **Yerel Geliştirme Ortamları**:
   - Geliştiriciler, bir uygulamanın tüm bileşenlerini (örneğin, web sunucusu, veritabanı, önbellek) yerel makinede kolayca simüle edebilir.
   - Örnek: Bir Python Flask uygulaması ve PostgreSQL veritabanını birlikte çalıştırmak için Docker Compose kullanılır.
   - Avantaj: Her geliştirici, aynı ortamı hızlıca oluşturabilir, böylece "benim makinemde çalışıyor" sorunları azalır.

2. **Test Ortamları**:
   - CI/CD süreçlerinde, testler için tutarlı ve izole edilmiş ortamlar oluşturmak için kullanılır.
   - Örnek: Bir uygulamanın birim testlerini çalıştırmak için gereken bağımlılıklar (örneğin, Redis, MySQL) Docker Compose ile hızlıca başlatılır.
   - Avantaj: Testler için temiz ve tekrarlanabilir ortamlar sağlar.

3. **Çoklu Konteyner Uygulamalarının Yönetimi**:
   - Birden fazla konteynerden oluşan karmaşık uygulamaları koordine eder (örneğin, bir web uygulaması, API, veritabanı ve mesaj kuyruğu).
   - Örnek: Bir WordPress sitesi için bir konteynerde WordPress, diğerinde MySQL çalıştırılır.
   - Avantaj: Konteynerler arasındaki ağ bağlantıları ve bağımlılıklar otomatik olarak yönetilir.

4. **Prototip ve Demo Ortamları**:
   - Hızlıca bir uygulamanın prototipini veya demosunu oluşturmak için kullanılır.
   - Örnek: Bir mikro servis mimarisini demo yapmak için her bir servisi ayrı bir konteynerde çalıştırabilirsiniz.
   - Avantaj: Kurulum süreci basitleşir ve ortam hızlıca paylaşılabilir.

5. **Eğitim ve Öğrenme**:
   - Yeni teknolojileri veya hizmetleri öğrenmek için farklı konteyner kombinasyonlarını kolayca test etmek için kullanılır.
   - Örnek: Bir geliştirici, bir Node.js uygulaması ile MongoDB’yi entegre etmeyi öğrenmek için Docker Compose kullanabilir.
   - Avantaj: Gerçek bir üretim ortamına ihtiyaç duymadan teknolojileri deneme imkanı sunar.

---

### Docker Compose Örneği

Aşağıda, bir web uygulaması (Node.js) ve bir veritabanı (PostgreSQL) içeren basit bir `docker-compose.yml` dosyası örneği verilmiştir:

```yaml
version: '3.8'
services:
  web:
    image: node:16
    ports:
      - "3000:3000"
    volumes:
      - ./app:/app
    working_dir: /app
    command: npm start
    depends_on:
      - db
    environment:
      - DATABASE_URL=postgres://user:password@db:5432/mydb
  db:
    image: postgres:13
    environment:
      - POSTGRES_USER=user
      - POSTGRES_PASSWORD=password
      - POSTGRES_DB=mydb
    volumes:
      - db-data:/var/lib/postgresql/data
volumes:
  db-data:
```

**Açıklama:**
- **web servisi**: Node.js uygulaması çalıştırır, 3000 portunu dışarıya açar ve `db` servisine bağlıdır.
- **db servisi**: PostgreSQL veritabanı çalıştırır ve veri kalıcılığı için bir volume kullanır.
- **Komut**: `docker-compose up` komutuyla bu iki konteyner başlatılır, ağ bağlantıları otomatik olarak kurulur.

**Çalıştırma:**
```bash
docker-compose up -d
```
Bu komut, her iki konteyneri arka planda başlatır ve uygulama 3000 portunda erişilebilir olur.

---

### Docker Compose'un Sınırları
- **Üretim Ortamları**: Docker Compose, genellikle geliştirme ve test için uygundur. Üretim ortamlarında, Kubernetes veya Docker Swarm gibi daha gelişmiş orkestrasyon araçları tercih edilir.
- **Ölçeklendirme**: Docker Compose, basit ölçeklendirme (örneğin, `docker-compose scale web=3`) destekler, ancak karmaşık ölçeklendirme veya yüksek erişilebilirlik için uygun değildir.
- **Platform Bağımlılığı**: Docker Compose, Docker’ın kurulu olduğu her ortamda çalışır, ancak yalnızca Docker konteynerlerini destekler.

---

### Özet
Docker Compose, çoklu konteyner uygulamalarını tanımlamak ve yönetmek için güçlü ve kullanımı kolay bir araçtır. Başlıca kullanım alanları, yerel geliştirme, test ortamları ve prototip oluşturmadır. YAML tabanlı deklaratif yapısı, geliştiricilere tutarlı, taşınabilir ve otomatikleştirilmiş ortamlar sunar. Ancak, üretim ortamları için daha gelişmiş araçlar gerekebilir.
