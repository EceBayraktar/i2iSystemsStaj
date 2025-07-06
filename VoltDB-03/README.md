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

# 3. VoltDB SQL Arayüzü ile Test
  
  docker exec -it node1-clean sqlcmd

Açılan komut satırında sırasıyla aşağıdaki SQL komutlarını girin:

CREATE TABLE mth3902 (
  id BIGINT NOT NULL,
  start_date_epoch BIGINT,
  create_user VARCHAR(32),
  CONSTRAINT mth3902_pk PRIMARY KEY(id)
);

PARTITION TABLE mth3902 ON COLUMN id;

INSERT INTO mth3902 (id, start_date_epoch, create_user) VALUES (1, 1698295044, 'MENNAN');
INSERT INTO mth3902 (id, start_date_epoch, create_user) VALUES (2, 1698295088, 'ERKUT');

SELECT * FROM mth3902;

![image](https://github.com/user-attachments/assets/2a81b505-0cb9-4f01-bc96-310b98437f1e)


# 4. Web UI ile VoltDB'ye Erişim

http://<GCP_VM_DIŞ_IP_ADRESİ>:8082
Örn: http://34.173.93.147:8082
![Sorgu](https://github.com/user-attachments/assets/23604423-e8e3-4f4a-8a77-5fae9070e079)

# 5. DBeaver ile VoltDB'ye Bağlantı

DBeaver uygulamasında:
Database → Driver Manager → New
Driver Name: VoltDB
Class Name: org.voltdb.jdbc.Driver
Add File: voltdbclient-14.1.0.jar dosyasını ekleyin

URL Template:
jdbc:voltdb://{host}:{port}
JDBC URL:jdbc:voltdb://<GCP_VM_EXTERNAL_IP>:21213
Username ve Password: Boş bırakın
Test Connection: "Success" mesajı alırsanız bağlandı demektir.


![WEBvoltdb](https://github.com/user-attachments/assets/8190e105-676c-40ae-8d57-f8d446e162e5)

![Shema](https://github.com/user-attachments/assets/2f77371a-e18c-4e4b-8da1-1e701d62fbd8)
