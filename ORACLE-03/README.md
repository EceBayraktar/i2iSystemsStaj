# ORACLEDB-EX-02: Oracle Database XE Setup with Docker

## 📌 Definition

This exercise demonstrates how to:
- Download and run Oracle Database 21c XE in a Docker container
- Connect to it via SQL*Plus, DBeaver, and Oracle Enterprise Manager (EM)
- Create a sample table named `BOOK`

---

## 🧰 Prerequisites

- Docker installed on local/cloud (GCP/AWS) machine
- Git installed

---

## 🔧 Installation & Setup

### 1. Clone Oracle Docker Repository
```bash
git clone https://github.com/oracle/docker-images.git
cd docker-images/OracleDatabase/SingleInstance/dockerfiles
```

## 2. Build Oracle XE Docker Image
```bash
./buildContainerImage.sh -v 21.3.0 -x
```

## 3. Run Oracle XE Container
```bash
docker run --name oraclexe -p 1521:1521 -p 5500:5500 -e ORACLE_PWD=ORACLE oracle/database:21.3.0-xe
```
## 4. Using SQL*Plus (inside container)
``` bash
docker exec -it oraclexe bash
sqlplus sys/ORACLE@//localhost:1521/XE as sysdba

-- Check database name
SQL> SELECT name FROM v$database;
```
## Access Oracle Enterprise Manager
``` bash
Open the following URL in a browser:
https://<your-external-ip>:5500/em
Login as: SYS, password: ORACLE, Role: SYSDBA
``` 

## 5.Create BOOK Table
``` bash
Switch to SQL Editor and run:

CREATE TABLE BOOK (
  ID NUMBER,
  NAME VARCHAR2(128),
  ISBN VARCHAR2(32),
  CREATE_DATE DATE DEFAULT SYSDATE
);
``` 
