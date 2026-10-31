# todolist-app

Modern, minimalist ve yüksek performanslı **Yapılacaklar Listesi (To-Do List)** uygulaması.  
**React 19**, **TypeScript**, **Vite** ve **Capacitor** ile geliştirilmiş olup; hem **PWA (Progressive Web App)** hem de yerel **Android (.apk)** ve **Android Ana Ekran Widget'ı (Home Screen Widget)** desteğine sahiptir.

---

## ✨ Özellikler

* **🎨 Modern & Pastel Arayüz:** Arduvaz mavisi (`#74a8c8`) ve adaçayı yeşili (`#5ebb94`) ile göz yormayan, estetik tasarım sistemi.
* **🌙 / ☀️ Koyu & Açık Tema (Dark Mode):**
  * Telefonun veya işletim sisteminin kendi karanlık/aydınlık ayarını (`prefers-color-scheme`) otomatik algılar.
  * İstenildiği zaman başlıktaki tema butonu ile manuel olarak **Karanlık (Dark)**, **Aydınlık (Light)** veya **Otomatik (Auto)** modları arasında geçiş yapılabilir.
  * Tercih tarayıcı hafızasında saklanır.
* **📱 Android Ana Ekran Widget'ı (Home Screen Widget):**
  * Uygulamadaki görevleri ve durumlarını doğrudan telefonun ana ekranında listeler.
  * Tamamlanan görevleri yeşil tik ve üstü çizili metinle, bekleyen görevleri mavi halka ile gösterir.
  * Başlıkta anlık durum özeti sunar (*"2 active · 3 total"* veya *"All done"*).
  * Widget'a dokunulduğunda doğrudan uygulamayı açar.
* **⚡ Canlı Köprü (Capacitor Bridge):**
  * React tarafındaki tüm görev değişiklikleri (`add`, `toggle`, `delete`), özel Capacitor Plugin (`TodoWidgetPlugin`) aracılığıyla Android yerel `SharedPreferences` alanına ve widget'a anında yansıtılır.
* **💾 Kesintisiz Kayıt (Save System):**
  * `localStorage` entegrasyonu sayesinde sayfa yenilendiğinde, tarayıcı veya telefon kapatıldığında hiçbir görev kaybolmaz.
* **🔍 Dinamik Filtreleme:**
  * **All (Tümü)**, **Active (Bekleyenler)** ve **Completed (Tamamlananlar)** sekmeleriyle görevleri anında filtreleme.
* **📲 PWA Desteği (Yüklenebilir Web Uygulaması):**
  * Manifest ve Service Worker (`sw.js`) sayesinde hem Android (Chrome) hem de iOS (Safari) cihazlara herhangi bir mağaza olmadan doğrudan yüklenebilir ve çevrimdışı (offline) çalışır.

---

## 🛠️ Teknoloji Yığını

| Katman | Teknoloji |
|---|---|
| **Frontend Framework** | React 19 + TypeScript |
| **Derleme & HMR** | Vite |
| **Stil & Tasarım** | Vanilla CSS (CSS Variables, Flexbox/Grid, Dark Mode Tokens) |
| **Linter** | Oxlint |
| **Mobil Çatı** | Capacitor 8 |
| **Android Native** | Java, AppWidgetProvider, RemoteViewsService |

---

## 🚀 Yerel Geliştirme (Local Setup)

### 1. Bağımlılıkları Yükleyin
```bash
npm install


