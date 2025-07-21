###  Yaygın olarak kullanılan 10 git komutunu yazın ve açıklayın.


Aşağıda, Git versiyon kontrol sisteminde en yaygın kullanılan 10 komutu ve her birinin ne işe yaradığını Türkçe olarak açıklıyorum. Bu komutlar, yazılım geliştirme süreçlerinde sıkça kullanılır ve kod yönetimi, dallanma, birleştirme ve işbirliği için temel işlevler sunar.

---

### 1. **git init**
   - **Açıklama**: Yeni bir Git deposu oluşturur. Mevcut bir dizini Git deposuna dönüştürür ve versiyon kontrolünü başlatır.
   - **Kullanım**:
     ```bash
     git init
     ```
   - **Örnek Senaryo**: Bir projeye başlamak için yerel bir dizinde Git deposu oluşturmak.
   - **Not**: Bu komut, `.git` dizinini oluşturur ve projeyi izlemeye başlar.

---

### 2. **git clone**
   - **Açıklama**: Uzak bir Git deposunu (örneğin, GitHub, GitLab) yerel makineye kopyalar.
   - **Kullanım**:
     ```bash
     git clone <depo-url>
     ```
   - **Örnek Senaryo**: GitHub’daki bir projeyi (`https://github.com/user/repo.git`) yerel makineye indirmek.
   - **Not**: Deponun tüm geçmişi ve dalları yerel makineye kopyalanır.

---

### 3. **git add**
   - **Açıklama**: Değiştirilen dosyaları veya yeni dosyaları indeks (staging area) alanına ekler, böylece bir sonraki commit’e hazır hale gelir.
   - **Kullanım**:
     ```bash
     git add <dosya>  # Belirli bir dosya
     git add .        # Tüm değişiklikler
     ```
   - **Örnek Senaryo**: `app.py` dosyasındaki değişiklikleri commit’e hazırlamak için `git add app.py`.
   - **Not**: Yalnızca eklenen dosyalar commit edilir.

---

### 4. **git commit**
   - **Açıklama**: İndeks alanındaki değişiklikleri yerel depoya kaydeder ve bir commit oluşturur. Her commit, bir mesajla birlikte kaydedilir.
   - **Kullanım**:
     ```bash
     git commit -m "Değişiklik mesajı"
     ```
   - **Örnek Senaryo**: Yeni bir özelliği tamamladıktan sonra `git commit -m "Login özelliği eklendi"` ile kaydetmek.
   - **Not**: `-a` bayrağıyla (`git commit -am "mesaj"`) doğrudan takip edilen dosyaları commit edebilirsiniz.

---

### 5. **git push**
   - **Açıklama**: Yerel depodaki commit’leri uzak depoya (örneğin, GitHub) gönderir.
   - **Kullanım**:
     ```bash
     git push origin <dal-adı>
     ```
   - **Örnek Senaryo**: Yerel `main` dalındaki değişiklikleri GitHub’a göndermek için `git push origin main`.
   - **Not**: Uzak depo bağlantısı (`origin`) önceden ayarlanmış olmalıdır.

---

### 6. **git pull**
   - **Açıklama**: Uzak depodan değişiklikleri çeker ve yerel depoyu günceller. Genellikle `fetch` ve `merge` işlemlerini birleştirir.
   - **Kullanım**:
     ```bash
     git pull origin <dal-adı>
     ```
   - **Örnek Senaryo**: Takım arkadaşlarının `main` dalına eklediği değişiklikleri almak için `git pull origin main`.
   - **Not**: Çakışmalar (conflicts) oluşursa, manuel olarak çözülmesi gerekir.

---

### 7. **git branch**
   - **Açıklama**: Mevcut dalları listeler, yeni bir dal oluşturur veya bir dalı siler.
   - **Kullanım**:
     ```bash
     git branch              # Mevcut dalları listele
     git branch <dal-adı>    # Yeni dal oluştur
     git branch -d <dal-adı> # Dal sil
     ```
   - **Örnek Senaryo**: Yeni bir özellik için `git branch feature/login` ile dal oluşturmak.
   - **Not**: `-a` bayrağıyla uzak dallar da listelenebilir (`git branch -a`).

---

### 8. **git checkout**
   - **Açıklama**: Çalışma dalını değiştirir veya belirli bir commit’e geçer. Yeni dallar oluştururken de kullanılabilir.
   - **Kullanım**:
     ```bash
     git checkout <dal-adı>           # Dala geç
     git checkout -b <dal-adı>        # Yeni dal oluştur ve geç
     git checkout <commit-hash>       # Belirli bir commit’e geç
     ```
   - **Örnek Senaryo**: `feature/login` dalına geçmek için `git checkout feature/login`.
   - **Not**: `git switch` (daha yeni bir komut) dallar arasında geçiş için alternatif olarak kullanılabilir.

---

### 9. **git merge**
   - **Açıklama**: Bir dalın değişikliklerini başka bir dala birleştirir.
   - **Kullanım**:
     ```bash
     git merge <dal-adı>
     ```
   - **Örnek Senaryo**: `feature/login` dalını `main`’e birleştirmek için önce `main`’e geçip (`git checkout main`), ardından `git merge feature/login` çalıştırmak.
   - **Not**: Çakışmalar olursa, manuel olarak çözülmesi ve ardından commit edilmesi gerekir.

---

### 10. **git status**
   - **Açıklama**: Çalışma dizinindeki değişikliklerin durumunu gösterir (hangi dosyalar değiştirildi, indekslendi veya commit edilmeyi bekliyor).
   - **Kullanım**:
     ```bash
     git status
     ```
   - **Örnek Senaryo**: Hangi dosyaların değiştirildiğini veya commit’e hazır olduğunu görmek için `git status`.
   - **Not**: Değişikliklerin (staged, unstaged) ve dal durumunun özetini sağlar.

---

### Özet
Bu 10 Git komutu, versiyon kontrolünün temel taşlarını oluşturur:
- **Başlangıç ve Depo Yönetimi**: `git init`, `git clone`
- **Değişiklik Yönetimi**: `git add`, `git commit`, `git status`
- **Uzak Depo İşlemleri**: `git push`, `git pull`
- **Dal Yönetimi**: `git branch`, `git checkout`, `git merge`

Bu komutlar, geliştiricilerin kodlarını yönetmesini, işbirliği yapmasını ve değişiklikleri güvenli bir şekilde takip etmesini sağlar. Daha ileri düzey işlemler için `git rebase`, `git stash` veya `git log` gibi ek komutlar da öğrenilebilir.
