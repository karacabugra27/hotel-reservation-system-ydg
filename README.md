# Hotel Reservation System YDG

## Selenium UI Testleri (Kisa Rehber)

### Calistirma
- Backend: `./mvnw spring-boot:run`
- Frontend: `cd frontend && npm install && npm run dev`
- Uygulama varsayilan olarak `http://localhost:8080` adresinden acilmalidir.
- Farkli adres icin: `APP_BASE_URL=http://localhost:5173`
- Frontend icin backend adresi: `VITE_API_BASE_URL=http://localhost:8080`
- Varsayilan akademik musteri: `VITE_DEFAULT_CUSTOMER_ID=1`

### Selenium Testlerini Yerelde Calistirma
- Selenium testleri browser uzerinden calisir.
- Komut: `./mvnw -Dtest=AvailableRoomsSeleniumTest -DfailIfNoTests=false verify`
- Headless mod: `SELENIUM_HEADLESS=true ./mvnw -Dtest=AvailableRoomsSeleniumTest -DfailIfNoTests=false verify`

### CI/CD (Jenkins) Notu
- Jenkins uzerinde testler headless calistirilir.
- Ornek: `SELENIUM_HEADLESS=true ./mvnw verify`
- Backend ve frontend servisleri ayakta olmalidir (UI gercek tarayici ile test edilir).

### Neden data-testid?
- Selenium selector'leri icin stabil bir hedef saglar.
- CSS siniflari veya rastgele id degisikligi testleri bozmaz.

## UI (Tailwind) Notlari
- Tailwind, akademik ve temiz bir arayuz icin hafif bir cozumdur.
- Indigo (guven) ve emerald (basari) renkleri kullanildi.
- Kartlar, formlar ve aksiyon butonlari icin modern, sade bir gorunum saglandi.

## Jenkins Uzerinde UI Testleri
- Jenkins pipeline icin headless mod kullanilir.
- Frontend ve backend servisleri ayakta olmalidir.
- Ornek: `SELENIUM_HEADLESS=true APP_BASE_URL=http://localhost:5173 ./mvnw verify`

## Selenium icin data-testid Neden Kritik?
- UI degisikliklerinde testlerin bozulmasini engeller.
- Selektorler yalnizca islevsel elementlere bagli kalir.

## Eklenen Akademik Ozellikler
- Oda Detay Sayfasi: `/rooms/:id`
- Rezervasyon Listesi ve Iptal: `/reservations`
- Oda secimi ve rezervasyon akisi: secilen oda ID dogrulanir ve gosterilir.
- Oda uygunluk yaniti kapasite ve fiyat bilgisiyle zenginlestirildi.
