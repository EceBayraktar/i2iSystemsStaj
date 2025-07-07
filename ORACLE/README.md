# Oracle Database XE Docker Kurulumu ve Kullanımı

## Proje Açıklaması
Bu proje kapsamında Oracle Database 21c Express Edition (XE) Docker container olarak çalıştırılmıştır.  
Docker Hub’dan resmi Oracle XE image’i çekilip, container oluşturulmuş ve Oracle SQL*Plus ile bağlantı sağlanmıştır.  
Ayrıca Oracle Enterprise Manager (EM) web arayüzüne erişim sağlanmıştır.

---

## Gereksinimler
- Docker Engine yüklü ve çalışır durumda
- İnternet bağlantısı (Docker Hub’dan image çekmek için)
- (Opsiyonel) Bulut ortamında çalıştırmak için uygun izinler ve port açma işlemleri

---

## Kurulum Adımları

# 1. Oracle Docker Image’i İndirme ve Oluşturma
Resmi Oracle Docker image’i kullanmak için öncelikle repository klonlanır ve image build edilir:

```bash
git clone https://github.com/oracle/docker-images.git
cd docker-images/OracleDatabase/SingleInstance/dockerfiles
./buildContainerImage.sh -v 21.3.0 -x




#**2. Docker Container Çalıştırma **
docker run --name oraclexe -p 1521:1521 -p 5500:5500 -e ORACLE_PWD=ORACLE -d oracle/database:21.3.0-xe


#**4. Oracle SQL*Plus’a Bağlanma**
docker exec -it oraclexe bash
bash-4.2# sqlplus sys/ORACLE@//localhost:1521/XE as sysdba
SQL> select name from v$database;

#**5. Oracle Enterprise Manager Web Arayüzüne Erişim**
Tarayıcıdan aşağıdaki adrese git:

Localde çalışıyorsa:
https://localhost:5500/em

Bulut ortamındaysa:
https://<sunucu_ip_adresi>:5500/em

İlk bağlantıda güvenlik uyarısını geç (self-signed sertifika).
![OracleWeb](https://github.com/user-attachments/assets/b2316e8c-6063-4030-a164-b63ac816b073)
![DB](https://github.com/user-attachments/assets/a5099330-7e5c-4aa7-8b37-c3ea12df4fd3)
![Oracledb](https://github.com/user-attachments/assets/aff50c4d-1902-4b66-9b90-7b82ccb91f8b)
