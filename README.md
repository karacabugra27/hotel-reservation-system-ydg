## Tests Overview

### ReservationServiceImplTest.java

- **odaDoluysaRezervasyonYapilmamali**  
  Aynı tarihte oda doluysa rezervasyon yaratmayı engeller ve repository `save` çağrılmaz.

- **odaBulunamazsaHataVermeli**  
  Oda bulunamazsa `createReservation` hata fırlatır.

- **musteriBulunamazsaHataVermeli**  
  Müşteri id’si verilip bulunamazsa hata fırlatılır ve kayıt oluşturulmaz.

- **basariliRezervasyon**  
  Uygun oda ve müşteri ile rezervasyon oluşturulur, DTO alanları (oda numarası, status, rezervasyon kodu) doğrulanır.

- **musteriBilgileriEksikseHataVermeli**  
  CustomerId yokken ad, soyad veya telefon eksikse validation hatası döner.

- **musteriBilgileriIleRezervasyonOlusturulabilir**  
  CustomerId yokken ad, soyad ve telefon ile yeni müşteri kaydı oluşturulur ve rezervasyon kaydedilir.

- **musteriIdVarkenYeniMusteriOlusturulmamali**  
  CustomerId varken yeni müşteri oluşturulmaz.

- **rezervasyonKoduIleGetirilmeli**  
  Rezervasyon kodu ile yapılan aramada DTO döndüğü doğrulanır.

- **reservasyonBulunamazsaHataVermeli**  
  İlgili id veya kod bulunamazsa hata fırlatılır.

- **rezervasyonIptalEdilmeli**  
  İptal işleminde status `CANCELED` olur ve kayıt güncellenir.

- **rezervasyonCheckInYapilabilmeli**  
  Check-in çağrısı status’u `IN_PROGRESS` yapar.

- **rezervasyonCheckOutYapilabilmeli**  
  Check-out çağrısı status’u `COMPLETED` yapar.

- **odayaGoreRezervasyonTarihleriDonmeli**  
  Odaya göre rezervasyon tarih aralıkları doğru şekilde DTO’ya map edilir.

---

### RoomServiceImplTest.java

- **musaitOdalarRoomResponseDtoOlarakDonmeli**  
  `AVAILABLE` durumundaki odaların doğru DTO alanlarıyla döndüğü kontrol edilir.

---

### PaymentServiceImplTest.java

- **rezervasyonBulunamazsaHataFirlatmali**  
  Geçersiz rezervasyon id’si ile ödeme yapılamaz.

- **basariliOdemeKaydiYapilmali**  
  Ödeme kaydı oluşturulur, status `PAID` olur ve Payment alanları doğru şekilde set edilir.

---

### HotelReservationSystemYdgApplicationTests.java

- **contextLoads**  
  Spring context’in test profiliyle başarıyla ayağa kalktığını doğrular.

---

## Controller Tests (MockMvc)

### PaymentControllerTest.java

- **makePaymentBasariliOldugundaResponseOkDonmeli**  
  Controller, servisten gelen DTO’yu 200 OK response ile döner.

---

### ReservationControllerTest.java

- **createReservationBasariliOldugundaResponseDonmeli**  
  Rezervasyon oluşturma isteği 200 OK ve DTO döner.

- **checkInCagrisiServiseDelegasyonYapmali**  
  Check-in isteğinin servise delegasyon yaptığı doğrulanır.

- **checkOutCagrisiServiseDelegasyonYapmali**  
  Check-out isteğinin servise delegasyon yaptığı doğrulanır.

- **rezervasyonKoduIleSorguServiseDelegasyonYapmali**  
  Rezervasyon kodu ile sorgu isteği servise yönlendirilir.

- **odayaGoreRezervasyonTarihleriServiseDelegasyonYapmali**  
  Oda id’sine göre tarih aralıklarının servisten çekildiği doğrulanır.

---

### RoomControllerTest.java

- **getAvailableRoomsServisiCagiripListeDonmeli**  
  `AVAILABLE` odalar servisten alınır ve 200 OK response döner.

---

## Integration Tests

### ReservationFlowIntegrationTest.java

- **rezervasyonOlusturuldugundaDtoAlanlariDonmeli**  
  Rezervasyon oluşturulduğunda response alanlarının doğru döndüğü doğrulanır.

- **rezervasyonKoduIleSorguBasarili**  
  Kod ile sorguda 200 OK ve doğru müşteri/rezervasyon bilgileri döner.

- **rezervasyonKoduBulunamazsaBadRequestDoner**  
  Geçersiz kod için 400 Bad Request ve hata mesajı döner.

- **odayaGoreRezervasyonlarIptalleriDislar**  
  İptal edilen rezervasyonlar listeleme sonuçlarından çıkarılır.

- **odemeYapilmadanCheckInYapilamaz**  
  Ödeme yapılmadan check-in isteği 4xx response döner.

- **odemeSonrasiCheckInVeCheckOutAkisiBasarili**  
  Ödeme → check-in → check-out akışı başarılı olur ve status `COMPLETED` olarak güncellenir.

---

### RoomAvailabilityIntegrationTest.java

- **sadeceMusaitOdalarDoner**  
  `/room/getAvailableRooms` endpoint’i yalnızca `AVAILABLE` odaları döner.

---

### PaymentIntegrationTest.java

- **priceAlanBosOlursaValidationHatasiDoner**  
  Price alanı boşsa 400 ve validasyon mesajı döner.

- **olmayanRezervasyonaOdemeIsConflictDoner**  
  Rezervasyon bulunamazsa ödeme isteği 409 Conflict döner.

---

### ReservationErrorIntegrationTest.java

- **ayniOdaIcinCakisanRezervasyonIsConflictDoner**  
  Aynı oda için tarih çakışması durumunda 409 Conflict ve hata mesajı döner.

- **olmayanRezervasyonCheckOutIsConflictDoner**  
  Var olmayan rezervasyonla check-out isteği 409 Conflict döner.

- **musteriBilgileriEksikseBadRequestDoner**  
  Eksik müşteri bilgileri ile istek atıldığında 400 Bad Request döner.

---

## Test Configuration

### TestSecurityConfig.java

- Test ortamında security’yi bypass etmek için tüm isteklere izin veren `SecurityFilterChain` sağlar.

---

## Selenium (E2E) Tests

### BaseSeleniumTest.java

- Selenium testleri için base URL ve headless mod ayarları environment variable’lardan alınır.

---

### HomePageSeleniumTest.java

- **anaSayfaYuklenirVeCtaGorunur**  
  Ana sayfa başlığı ve CTA butonlarının görünür olduğu doğrulanır.

---

### AvailableRoomsSeleniumTest.java

- **musaitOdalarListelenebiliyor**  
  Odalar sayfası → oda detayı → rezervasyon akışı çalışır, özet görüntülenir ve kod ile lookup yapılır.

---

### RoomsFilterSeleniumTest.java

- **odaTipiFiltresiCalisir**  
  Oda tipi filtresi seçiminden sonra kartlarda doğru tip etiketleri görünür.

---

### ReservationCodeCopySeleniumTest.java

- **rezervasyonKoduKopyalanabilir**  
  Rezervasyon sonrası “Kodu Kopyala” butonuna basıldığında sonuç metni değişir.

---

### ReservationDisabledDatesSeleniumTest.java

- **doluTarihSecilemez**  
  Dolu tarihler datepicker üzerinde disable olarak görünür.

---

### ReservationLookupInvalidSeleniumTest.java

- **hataliKodIleSorguHataVerir**  
  Geçersiz kod ile sorgulama yapıldığında hata mesajı gösterilir.

---

### ReservationLookupValidSeleniumTest.java

- **rezervasyonKoduIleSorguBasarili**  
  Geçerli kod ile sorgulamada rezervasyon özet kartı görüntülenir.

---

### ReservationNavigationSeleniumTest.java

- **odaDetayindanRezervasyonSayfasinaGidilir**  
  Oda detay sayfasından rezervasyon sayfasına başarılı şekilde geçiş yapılır.

---

### RoomDetailSeleniumTest.java

- **odaDetaySayfasiBilgileriGosterir**  
  Oda detay sayfasında oda başlığı, numarası ve kapasite bilgileri görünür.

---

### DatePickerPastDaysSeleniumTest.java

- **gecmisTarihSecilemez**  
  Datepicker üzerinde geçmiş tarihler disabled olarak gösterilir.
