### Configuration as Code (CaC) ve Infrastructre as Code (IaC) kavramlarını bir örnekle açıklayınız.
### Configuration as Code (CaC) Nedir?

**Configuration as Code (CaC)**, sistem konfigürasyonlarının kod olarak tanımlanması ve yönetilmesi yaklaşımıdır. Bu yöntem, sistem ayarlarını, yapılandırma dosyalarını veya hizmet konfigürasyonlarını manuel olarak düzenlemek yerine, bunları programatik olarak kod dosyalarıyla tanımlamayı ve otomatikleştirmeyi sağlar. CaC, genellikle yazılım geliştirme süreçlerinde DevOps ve CI/CD (Sürekli Entegrasyon/Sürekli Dağıtım) yaklaşımlarıyla birlikte kullanılır. Bu sayede konfigürasyonlar versiyonlanabilir, tekrar kullanılabilir ve otomatik olarak uygulanabilir hale gelir.

**CaC'nin Özellikleri:**
- **Versiyon Kontrolü:** Konfigürasyonlar Git gibi versiyon kontrol sistemlerinde saklanabilir.
- **Otomasyon:** Konfigürasyonlar otomatik olarak uygulanabilir, insan hatası azalır.
- **Tekrarlanabilirlik:** Aynı konfigürasyon farklı ortamlara kolayca uygulanabilir.
- **İzlenebilirlik:** Değişiklikler takip edilebilir ve geri alınabilir.

**CaC Örneği:**
Bir web sunucusunun (örneğin Nginx) konfigürasyonunu CaC ile yönetmek için Ansible gibi bir araç kullanılabilir. Aşağıdaki Ansible playbook, bir Nginx sunucusunun konfigürasyon dosyasını tanımlar:

```yaml
---
- name: Nginx konfigürasyonunu ayarla
  hosts: web_servers
  tasks:
    - name: Nginx konfigürasyon dosyasını kopyala
      copy:
        src: ./nginx.conf
        dest: /etc/nginx/nginx.conf
        owner: root
        group: root
        mode: '0644'
    - name: Nginx servisini yeniden başlat
      service:
        name: nginx
        state: restarted
```

Bu playbook, Nginx konfigürasyon dosyasını belirli bir sunucuya kopyalar ve servisi yeniden başlatır. `nginx.conf` dosyası, kod olarak saklanır ve versiyon kontrol sisteminde yönetilebilir.

---

### Infrastructure as Code (IaC) Nedir?

**Infrastructure as Code (IaC)**, altyapı bileşenlerinin (sunucular, ağlar, veritabanları, vb.) kod kullanılarak tanımlanması ve yönetilmesi yöntemidir. IaC, fiziksel veya sanal altyapıyı manuel olarak yapılandırmak yerine, bu süreçleri kod tabanlı ve otomatik bir şekilde gerçekleştirmeyi sağlar. Bu yaklaşım, altyapının hızlı, tutarlı ve tekrarlanabilir şekilde oluşturulmasını sağlar.

**IaC'nin Özellikleri:**
- **Otomasyon:** Altyapı oluşturma ve yönetme süreçleri otomatikleştirilir.
- **Tutarlılık:** Aynı kod, farklı ortamlar için aynı altyapıyı oluşturur.
- **Hız:** Altyapı dağıtımı hızlı ve hatasız gerçekleşir.
- **Versiyon Kontrolü:** Altyapı tanımları kod olarak saklanır ve değişiklikler izlenebilir.

**IaC Örneği:**
AWS üzerinde bir EC2 sunucusu oluşturmak için Terraform kullanılabilir. Aşağıdaki Terraform kodu, bir AWS EC2 örneği oluşturur:

```hcl
provider "aws" {
  region = "us-east-1"
}

resource "aws_instance" "web_server" {
  ami           = "ami-0c55b159cbfafe1f0" # Amazon Linux 2 AMI
  instance_type = "t2.micro"

  tags = {
    Name = "WebServer"
  }
}
```

Bu kod, AWS'de bir EC2 sunucusu oluşturur. Kod çalıştırıldığında, Terraform belirtilen özelliklere sahip bir sunucu otomatik olarak başlatır. Bu kod, Git gibi bir versiyon kontrol sisteminde saklanabilir ve farklı ortamlar için tekrar kullanılabilir.

---

### CaC ve IaC Arasındaki Farklar
- **Kapsam:** IaC, altyapı bileşenlerini (sunucular, ağlar, vb.) oluşturur ve yönetirken; CaC, mevcut altyapı üzerindeki yazılım veya hizmet konfigürasyonlarını yönetir.
- **Odak:** IaC, altyapının kendisini oluşturur (örneğin, bir sunucu başlatma), CaC ise bu altyapı üzerindeki ayarları tanımlar (örneğin, sunucudaki bir uygulamanın yapılandırması).
- **Araçlar:** IaC için Terraform, AWS CloudFormation gibi araçlar; CaC için Ansible, Chef, Puppet gibi araçlar yaygın olarak kullanılır.

**Örnek Senaryo:**
Bir web uygulamasını dağıtmak istiyorsunuz:
1. **IaC ile**: Terraform kullanarak AWS'de bir EC2 sunucusu, bir VPC ve bir yük dengeleyici oluşturursunuz.
2. **CaC ile**: Ansible kullanarak bu EC2 sunucusuna Nginx kurar, gerekli konfigürasyon dosyasını yükler ve web uygulamasını dağıtırsınız.

Her iki yaklaşım da otomasyon ve tekrarlanabilirlik sağlar, ancak farklı katmanlarda çalışır.
