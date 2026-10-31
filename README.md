# 📝 To-Do List Mobile & Web App

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

### 1. Projeyi Klonlayın ve Bağımlılıkları Yükleyin
```bash
git clone https://github.com/zeynepberrasen/todolist-app.git
cd todolist
npm install
```

### 2. Geliştirme Sunucusunu Başlatın
```bash
npm run dev
```
Tarayıcınızda açmak için: [http://localhost:5173/](http://localhost:5173/)  
Aynı Wi-Fi ağındaki telefondan erişmek için: `npm run dev -- --host`

### 3. Web Sürümünü Derleyin
```bash
npm run build
```

---

## 🤖 Android Projesi & APK Üretimi

Capacitor entegrasyonu projenin `android/` klasöründe hazırdır.

### Web Değişikliklerini Android'e Aktarma
```bash
npm run build
npx cap sync android
```

### Android Studio ile Açma
```bash
npx cap open android
```
* **APK Çıktısı Alma:** Android Studio içinde **Build > Build Bundle(s) / APK(s) > Build APK(s)** seçeneğine tıklayın.
* Oluşturulan dosya: `android/app/build/outputs/apk/debug/app-debug.apk`

---

## 🧩 Android Ana Ekran Widget Yapısı

* **TodoWidgetPlugin.java:** React durumunu Android `SharedPreferences` alanına yazan ve widget güncellemesini tetikleyen Capacitor köprüsü.
* **TodoWidgetProvider.java:** Widget yaşam döngüsünü ve başlık istatistiklerini yöneten `AppWidgetProvider`.
* **TodoWidgetService.java:** Liste satırlarını (`RemoteViews`) üreten `RemoteViewsService` fabrikası.

---

## 📂 Proje Dizin Yapısı

```text
todolist/
├── android/                   # Capacitor yerel Android projesi
│   └── app/src/main/
│       ├── java/com/todolist/app/
│       │   ├── MainActivity.java
│       │   ├── TodoWidgetPlugin.java
│       │   ├── TodoWidgetProvider.java
│       │   └── TodoWidgetService.java
│       └── res/
│           ├── layout/todo_widget_layout.xml
│           ├── layout/todo_widget_item.xml
│           └── xml/todo_widget_info.xml
├── public/                    # PWA varlıkları, ikonlar ve service worker
│   ├── app-icon.svg
│   ├── favicon.svg
│   ├── manifest.json
│   └── sw.js
├── src/
│   ├── components/
│   │   ├── TodoFilter.tsx
│   │   ├── TodoForm.tsx
│   │   ├── TodoItem.tsx
│   │   └── TodoList.tsx
│   ├── services/
│   │   └── todoWidget.ts      # Capacitor köprü servisi
│   ├── App.css                # CSS değişkenleri & Dark Mode
│   ├── App.tsx
│   ├── main.tsx
│   └── types.ts
├── capacitor.config.ts        # Capacitor yapılandırması
├── package.json
└── README.md
```

---

## 📄 Lisans
Bu proje [MIT Lisansı](LICENSE) ile lisanslanmıştır. Dilediğiniz gibi kullanabilir ve geliştirebilirsiniz.
