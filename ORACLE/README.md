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





