### Docker Compose'da varsayılan ağ türü nedir ve hizmetler varsayılan olarak birbirleriyle nasıl iletişim kurar?
### Docker Compose'da Varsayılan Ağ Türü

Docker Compose'da varsayılan ağ türü **bridge** ağıdır. Her Docker Compose projesi (bir `docker-compose.yml` dosyası ile tanımlanan uygulama), otomatik olarak kendi izole edilmiş bir **bridge** ağı oluşturur. Bu ağ, yalnızca o Compose projesindeki servislerin birbirleriyle iletişim kurmasını sağlar ve dış dünyadan izole edilmiştir.

- **Bridge Ağı Özellikleri**:
  - Docker Compose, her proje için benzersiz bir ağ oluşturur (varsayılan ad: `<proje_adı>_default`).
  - Bu ağ, servislerin birbirleriyle kolayca iletişim kurmasını sağlar.
  - Dış erişim için, servislerin portları açıkça `ports` direktifiyle dışarıya açılmalıdır (örneğin, `80:80`).
  - Bridge ağı, aynı Docker host'unda çalışan konteynerler arasında iletişim için kullanılır.

---

### Servisler Arası İletişim

Docker Compose'da servisler, varsayılan olarak aşağıdaki mekanizmalarla birbirleriyle iletişim kurar:

1. **Servis Adları ile DNS Çözümlemesi**:
   - Docker Compose, her servise otomatik olarak bir DNS adı atar. Bu DNS adı, `docker-compose.yml` dosyasında tanımlanan servis adıyla aynıdır.
   - Örneğin, bir `web` servisi ve bir `db` servisi varsa, `web` servisi, `db` servisine `db` adını kullanarak erişebilir (örneğin, `http://db:5432`).
   - Bu, IP adreslerini manuel olarak belirtmeye gerek kalmadan servisler arası iletişimi kolaylaştırır.

2. **Varsayılan Bridge Ağı**:
   - Tüm servisler, aynı varsayılan bridge ağına bağlanır. Bu ağ, servislerin birbirine doğrudan erişmesini sağlar.
   - Örneğin, bir web uygulaması (`web`) bir veritabanına (`db`) bağlanmak için, `db` servis adını ve uygun portu kullanabilir (örneğin, `postgres://db:5432/mydb`).

3. **Bağımlılık Yönetimi**:
   - `depends_on` direktifiyle, bir servisin diğerine bağımlılığı belirtilebilir. Bu, bağımlı servislerin başlatılma sırasını kontrol eder, ancak iletişim için zorunlu değildir.
   - Örneğin, `web` servisi `db` servisine bağımlıysa, Docker Compose önce `db`’yi başlatır.

4. **Port ve Ağ Ayarları**:
   - Servisler, yalnızca `ports` direktifiyle dışarıya açık portlar aracılığıyla dış dünyayla iletişim kurar. Ancak, aynı ağdaki servisler, iç portlar üzerinden (örneğin, `5432` PostgreSQL için) doğrudan iletişim kurabilir.
   - Örnek: `web` servisi, `db` servisinin iç portu olan `5432`'ye erişebilir, dışarıya açılmadan.

---

### Örnek: Servisler Arası İletişim

Aşağıdaki `docker-compose.yml` dosyası, bir web uygulaması (`web`) ve bir veritabanı (`postgres`) örneğini içerir:

```yaml
version: '3.8'
services:
  web:
    image: node:16
    ports:
      - "3000:3000"
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
```

**Açıklama:**
- **Ağ**: Docker Compose, otomatik olarak `<proje_adı>_default` adında bir bridge ağı oluşturur (örneğin, proje adı `myapp` ise ağ `myapp_default` olur).
- **İletişim**:
  - `web` servisi, `db` servisine `db` DNS adıyla erişir (`postgres://user:password@db:5432/mydb`).
  - `db` servisi, PostgreSQL’in varsayılan portu olan `5432` üzerinden erişilebilir.
  - `web` servisi, dışarıya `3000` portuyla açılır, ancak `db` servisi yalnızca iç ağda erişilebilirdir (dışarıya açık port tanımlanmamıştır).

**Çalıştırma:**
```bash
docker-compose up -d
```

Bu komut, her iki servisi başlatır ve varsayılan bridge ağı üzerinden iletişim kurmalarını sağlar. `web` servisi, `db` servisine `db:5432` adresiyle bağlanır.

---

### Varsayılan Ağın Özellikleri ve Davranışı
- **İzolasyon**: Varsayılan bridge ağı, yalnızca aynı Compose projesindeki servisler için erişilebilirdir. Farklı Compose projeleri farklı ağlar kullanır ve birbirine erişemez (özel bir ağ tanımlanmadıkça).
- **Otomatik DNS**: Her servis, kendi adıyla ağda çözümlenir. Örneğin, `db` servisine `ping db` komutuyla erişilebilir.
- **Port İletimi**: Dış dünya ile iletişim için `ports` direktifi kullanılmalıdır. İç iletişimde ise portlar otomatik olarak erişilebilirdir.

---

### Özet
- **Varsayılan Ağ Türü**: Docker Compose’da varsayılan ağ, **bridge** ağıdır ve her proje için otomatik olarak oluşturulur.
- **Servisler Arası İletişim**: Servisler, servis adlarıyla (DNS çözümlemesiyle) ve varsayılan bridge ağı üzerinden iletişim kurar. İletişim, iç portlar aracılığıyla gerçekleşir ve `depends_on` ile başlatma sırası kontrol edilebilir.
- **Kullanım Kolaylığı**: Varsayılan ağ, geliştirme ve test ortamlarında servisler arası iletişimi kolaylaştırır, manuel ağ yapılandırması gerektirmez.

Eğer özel bir ağ türü (örneğin, `host` veya `overlay`) kullanmak isterseniz, `docker-compose.yml` dosyasında `networks` direktifiyle bu ayarları özelleştirebilirsiniz.
