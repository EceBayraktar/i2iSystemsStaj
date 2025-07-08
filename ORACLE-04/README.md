**Oracle JDBC Bağlantısı ve Kitap Veritabanına Kayıt Ekleme**
Proje Açıklaması
Bu proje, Java ile Oracle veritabanına bağlanarak BOOK tablosuna 100 adet kitap kaydı ekleyen basit bir uygulamadır.

**Önkoşullar**
Oracle Database (Docker veya Cloud ortamında) kurulu ve çalışıyor olmalı.

Oracle JDBC sürücüsü (ojdbc11.jar) projede mevcut.

Java 8+ yüklü olmalı.

Veritabanında BOOK tablosu oluşturulmuş veya aşağıdaki SQL komutlarıyla oluşturulabilir.

# 1. Oracle Veritabanı Bağlantısı
SYS AS SYSDBA ile bağlanma
Oracle’da yönetici yetkileriyle bağlanmak için kullanıcı adı "SYS AS SYSDBA" şeklinde yazılmalı.

Şifre Oracle kurulumunda belirlediğin SYS kullanıcısının şifresi olmalı.

# 2. BOOK Tablosunu Oluşturma
Aşağıdaki SQL komutunu kullanarak tabloyu oluştur:
```bash
CREATE TABLE BOOK (
    ID NUMBER PRIMARY KEY,
    NAME VARCHAR2(128),
    ISBN VARCHAR2(32),
    CREATE_DATE DATE DEFAULT SYSDATE
);
```
# 3. Java Kodunda Bağlantı Bilgileri
```bash
String jdbcUrl = "jdbc:oracle:thin:@//<host>:1521/<servicename>";
String username = "SYS AS SYSDBA";  // Yönetici kullanıcı
String password = "<SYS şifresi>";
```

![100KAYIT](https://github.com/user-attachments/assets/eeb4ffc4-ec84-40e4-9957-c4f090eb1615)
