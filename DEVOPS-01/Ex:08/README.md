### Kod ana dalda birleştirildiğinde dağıtım yapmak, ancak her çekme isteğinde testleri çalıştırmak için bir pipeline'ı nasıl yapılandırırsınız?

Bir CI/CD pipeline’ını, yalnızca ana dala (`main` branch) birleştirilen kod için dağıtım yapacak, ancak her pull request (PR) için testleri çalıştıracak şekilde yapılandırmak için, pipeline’ın dallara (branches) ve olaylara (events) göre koşullu olarak çalışmasını sağlayacak bir yapılandırma gerekir. Aşağıda, bu senaryoyu gerçekleştirmek için adımları ve örnek bir yapılandırmayı Türkçe olarak açıklıyorum. Örnek olarak **GitHub Actions** kullanılacak, çünkü bu, popüler ve esnek bir CI/CD aracıdır. Ancak prensipler, Jenkins, GitLab CI/CD veya CircleCI gibi diğer araçlara da uygulanabilir.

---

### Genel Yaklaşım
1. **Pipeline’ı Tetikleme Koşulları**:
   - **Pull Request’ler için**: Her pull request oluşturulduğunda veya güncellendiğinde yalnızca test adımları çalıştırılır.
   - **Main Branch için**: Kod `main` dalına birleştirildiğinde testler çalıştırılır ve ardından dağıtım (deployment) yapılır.
2. **Adımlar**:
   - Kod kalitesi ve test adımları (linting, birim testleri, entegrasyon testleri) her iki durumda da çalışır.
   - Dağıtım adımı sadece `main` dalı için çalışır.
3. **Koşullu Çalıştırma**: Pipeline, dalları ve olayları kontrol ederek hangi adımların çalışacağını belirler.

---

### Örnek GitHub Actions Pipeline’ı

Aşağıdaki `docker-compose.yml` dosyası, bir Python web uygulamasını (backend) ve bir Nginx frontend servisini çalıştıran bir yapılandırma içeriyor. Bu örneği temel alarak, bir GitHub Actions pipeline’ı oluşturuyorum. Pipeline, pull request’lerde testleri çalıştıracak ve sadece `main` dalına birleştirme olduğunda dağıtımı gerçekleştirecek.

#### `docker-compose.yml` (Referans için)
```yaml
version: '3.8'
services:
  backend:
    image: python:3.9
    volumes:
      - ./backend:/app
    working_dir: /app
    command: python -m http.server 5000
    expose:
      - "5000"
  frontend:
    image: nginx:latest
    ports:
      - "80:80"
    volumes:
      - ./nginx.conf:/etc/nginx/nginx.conf:ro
    depends_on:
      - backend
```

#### GitHub Actions Pipeline (`/.github/workflows/ci-cd.yml`)

```yaml
name: CI/CD Pipeline

on:
  pull_request:
    branches:
      - main
  push:
    branches:
      - main

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - name: Checkout code
        uses: actions/checkout@v4

      - name: Set up Python
        uses: actions/setup-python@v5
        with:
          python-version: '3.9'

      - name: Install dependencies
        run: |
          python -m pip install --upgrade pip
          pip install -r backend/requirements.txt

      - name: Run linting
        run: |
          pip install flake8
          flake8 backend/

      - name: Run unit tests
        run: |
          pip install pytest
          pytest backend/tests/

      - name: Set up Docker Compose
        run: |
          docker-compose -f docker-compose.yml up -d

      - name: Run integration tests
        run: |
          pip install requests
          python backend/tests/integration_tests.py

      - name: Tear down Docker Compose
        run: |
          docker-compose -f docker-compose.yml down

  deploy:
    needs: test
    runs-on: ubuntu-latest
    if: github.event_name == 'push' && github.ref == 'refs/heads/main'
    steps:
      - name: Checkout code
        uses: actions/checkout@v4

      - name: Log in to Docker Hub
        uses: docker/login-action@v3
        with:
          username: ${{ secrets.DOCKER_USERNAME }}
          password: ${{ secrets.DOCKER_PASSWORD }}

      - name: Build and push Docker images
        run: |
          docker-compose -f docker-compose.yml build
          docker-compose -f docker-compose.yml push

      - name: Deploy to production
        env:
          AWS_ACCESS_KEY_ID: ${{ secrets.AWS_ACCESS_KEY_ID }}
          AWS_SECRET_ACCESS_KEY: ${{ secrets.AWS_SECRET_ACCESS_KEY }}
          AWS_REGION: us-east-1
        run: |
          # Örnek: AWS ECS veya başka bir platforma dağıtım
          echo "Deploying to production..."
          # AWS CLI veya başka bir araçla dağıtım komutları
```

---

### Pipeline’ın Açıklaması

#### 1. **Tetikleyici Koşullar (`on`)**
- **`pull_request`**: Her pull request oluşturulduğunda veya güncellendiğinde (`main` dalına yönelik) pipeline tetiklenir ve sadece test işleri çalışır.
- **`push`**: Kod `main` dalına push edildiğinde (örneğin, bir PR birleştirildiğinde) hem test hem de dağıtım işleri çalışır.

#### 2. **Test İşi (`test`)**
Bu iş, hem pull request’lerde hem de `main` dalında çalışır:
- **Checkout code**: Kod deposunu çeker.
- **Set up Python**: Python 3.9 ortamını kurar.
- **Install dependencies**: Backend’in bağımlılıklarını (`requirements.txt`) yükler.
- **Run linting**: Kod kalitesini kontrol etmek için `flake8` ile statik analiz yapar.
- **Run unit tests**: `pytest` ile birim testlerini çalıştırır.
- **Set up Docker Compose**: `docker-compose.yml` dosyasını kullanarak backend ve frontend servislerini başlatır.
- **Run integration tests**: Örneğin, `requests` kütüphanesiyle backend’e HTTP istekleri göndererek entegrasyon testleri yapar.
- **Tear down Docker Compose**: Testlerden sonra konteynerleri durdurur.

#### 3. **Dağıtım İşi (`deploy`)**
Bu iş, sadece `main` dalına push olduğunda çalışır:
- **`needs: test`**: Dağıtım, test işinin başarılı olmasına bağlıdır.
- **`if: github.event_name == 'push' && github.ref == 'refs/heads/main'`**: Bu koşul, işin yalnızca `main` dalına push olayında çalışmasını sağlar.
- **Log in to Docker Hub**: Docker Hub’a kimlik bilgileriyle giriş yapar (gizli anahtarlar `secrets` ile saklanır).
- **Build and push Docker images**: `docker-compose.yml` dosyasındaki servisler için Docker imajlarını oluşturur ve Docker Hub’a yükler.
- **Deploy to production**: Örnek olarak AWS’ye dağıtım gösterilmiştir. Gerçek uygulamada, AWS ECS, Kubernetes veya başka bir platforma özel komutlar eklenir.

---

### Kod Kalitesi ve Güvenli Dağıtım için Ek Özellikler
- **Linting**: `flake8` ile kod kalitesini kontrol ederek okunabilirlik ve standartlara uygunluk sağlanır.
- **Testler**: Birim ve entegrasyon testleri, kodun doğru çalıştığını doğrular.
- **Koşullu Dağıtım**: `if` koşulu, dağıtımın yalnızca `main` dalında gerçekleşmesini garanti eder.
- **Gizli Anahtarlar**: `DOCKER_USERNAME`, `DOCKER_PASSWORD`, `AWS_ACCESS_KEY_ID` gibi hassas bilgiler, GitHub Secrets ile güvenli bir şekilde saklanır.
- **Bağımlılık Kontrolü**: `needs: test` ile dağıtım, testlerin başarılı olmasına bağlıdır.

---

### Diğer CI/CD Araçlarıyla Uygulama
Aynı mantık diğer araçlara da uygulanabilir:
- **Jenkins**: `Jenkinsfile` içinde `when { branch 'main' }` ile dağıtım adımı koşullandırılır.
- **GitLab CI/CD**: `.gitlab-ci.yml` içinde `only: - main` ve `except: - pull_request` kullanılarak benzer bir yapı kurulur.
- **CircleCI**: `.circleci/config.yml` içinde `filters` ile dallar kontrol edilir.

---

### Özet
Bu pipeline, pull request’lerde yalnızca testleri çalıştırır ve kod `main` dalına birleştirildiğinde otomatik olarak dağıtımı gerçekleştirir. GitHub Actions örneğinde, test adımları (`linting`, birim testleri, entegrasyon testleri) her iki durumda da çalışırken, dağıtım adımı yalnızca `main` dalına özgüdür. Kod kalitesini sağlamak için linting ve testler, güvenli dağıtım için koşullu çalıştırma ve gizli anahtarlar kullanılır. Bu yapı, yazılım geliştirme süreçlerini otomatikleştirir ve hata riskini azaltır.
