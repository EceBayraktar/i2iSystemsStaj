# VoltDB Kurulumu ve Kullanımı (Google Cloud + Docker)

Bu belge, Google Cloud üzerinde Docker ile VoltDB Community Edition'ı kurma, SQL işlemleri yapma ve DBeaver üzerinden bağlantı kurma adımlarını içermektedir.

---

## 🔧 1. Google Cloud VM Hazırlığı

- Makine Türü: e2-medium (en az 2 GB RAM önerilir)
- İşletim Sistemi: Debian veya Ubuntu tabanlı bir Linux dağıtımı
- Docker kurulu olmalıdır.

### 🚨 Gerekli Portlar (Firewall Rules)

Google Cloud > VPC Network > Firewall > "Create Firewall Rule" diyerek aşağıdaki TCP portlarını açın:

| Port   | Amaç                     |
|--------|--------------------------|
| 21213  | VoltDB JDBC bağlantısı   |
| 8082   | VoltDB Web UI (Dashboard)|

---

## 🐳 2. Docker ile VoltDB Kurulumu

Terminale aşağıdaki komutları sırasıyla yazın:

```bash
# Docker ağı oluştur
docker network create voltLocalCluster

# VoltDB image'ini indir
docker pull full360/docker-voltdb-ce

# VoltDB container'ı başlat
docker run -d \
  --name=node1-clean \
  -p 8082:8080 \
  -p 21213:21212 \
  --network=voltLocalCluster \
  full360/docker-voltdb-ce

## VoltDB'yi Test Et
Container’a bağlanıp SQL komutları ile test edin:

docker exec -it node1-clean sqlcmd







![image](https://github.com/user-attachments/assets/2a81b505-0cb9-4f01-bc96-310b98437f1e)

![WEBvoltdb](https://github.com/user-attachments/assets/8190e105-676c-40ae-8d57-f8d446e162e5)

![Shema](https://github.com/user-attachments/assets/2f77371a-e18c-4e4b-8da1-1e701d62fbd8)
