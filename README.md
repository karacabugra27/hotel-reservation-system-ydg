🧪 Unit Testler

📄 ReservationServiceImplTest.java

Rezervasyon iş kurallarını kapsar.
	•	odaDoluysaRezervasyonYapilmamali
Aynı tarihte oda doluysa rezervasyon engellenir, repo.save() çağrılmaz.
	•	odaBulunamazsaHataVermeli
Oda bulunamazsa createReservation hata fırlatır.
	•	musteriBulunamazsaHataVermeli
Geçersiz müşteri id’si ile rezervasyon yapılamaz, kayıt oluşturulmaz.
	•	basariliRezervasyon
Uygun oda + müşteri ile rezervasyon oluşturulur, DTO alanları (oda no, status, kod) doğrulanır.
	•	musteriBilgileriEksikseHataVermeli
CustomerId yokken ad/soyad/telefon eksikse validation hatası döner.
	•	musteriBilgileriIleRezervasyonOlusturulabilir
CustomerId yoksa müşteri bilgileriyle yeni müşteri oluşturulur ve rezervasyon kaydedilir.
	•	musteriIdVarkenYeniMusteriOlusturulmamali
CustomerId varken yeni müşteri kaydı yapılmaz.
	•	rezervasyonKoduIleGetirilmeli
Rezervasyon kodu ile aramada DTO döner.
	•	reservasyonBulunamazsaHataVermeli
Geçersiz id/kod için hata fırlatılır.
	•	rezervasyonIptalEdilmeli
İptalde status CANCELED olur ve kayıt güncellenir.
	•	rezervasyonCheckInYapilabilmeli
Check-in çağrısı status’u IN_PROGRESS yapar.
	•	rezervasyonCheckOutYapilabilmeli
Check-out çağrısı status’u COMPLETED yapar.
	•	odayaGoreRezervasyonTarihleriDonmeli
Odaya göre rezervasyon tarihleri doğru DTO’ya map edilir.

⸻

📄 RoomServiceImplTest.java
	•	musaitOdalarRoomResponseDtoOlarakDonmeli
AVAILABLE odalar doğru DTO alanlarıyla döner.

⸻

📄 PaymentServiceImplTest.java
	•	rezervasyonBulunamazsaHataFirlatmali
Geçersiz rezervasyon id’siyle ödeme yapılamaz.
	•	basariliOdemeKaydiYapilmali
Ödeme kaydı oluşturulur, status PAID olur ve alanlar doğrulanır.

⸻

🌐 Controller Testleri (MockMVC)

📄 ReservationControllerTest.java
	•	createReservation → 200 OK + DTO
	•	check-in → servise delegasyon
	•	check-out → servise delegasyon
	•	kod ile sorgu → servise delegasyon
	•	oda id’si ile tarih sorgusu → servise delegasyon

⸻

📄 PaymentControllerTest.java
	•	makePaymentBasariliOldugundaResponseOkDonmeli
Servisten gelen DTO 200 OK ile döner.

⸻

📄 RoomControllerTest.java
	•	getAvailableRoomsServisiCagiripListeDonmeli
AVAILABLE odalar servisten alınır ve 200 OK döner.

⸻

🔗 Integration Testleri

📄 HotelReservationSystemYdgApplicationTests.java
	•	contextLoads
Spring context’in test profiliyle ayağa kalktığını doğrular.

⸻

📄 ReservationFlowIntegrationTest.java
	•	Rezervasyon oluşturma → DTO doğrulama
	•	Kod ile sorgu → başarılı akış
	•	Geçersiz kod → 400 Bad Request
	•	İptal edilen rezervasyonlar listelenmez
	•	Ödeme olmadan check-in yapılamaz
	•	Ödeme → check-in → check-out → COMPLETED akışı

⸻

📄 RoomAvailabilityIntegrationTest.java
	•	/room/getAvailableRooms sadece AVAILABLE odaları döner.

⸻

📄 PaymentIntegrationTest.java
	•	price boş → 400 validation hatası
	•	rezervasyon yok → 409 Conflict

⸻

📄 ReservationErrorIntegrationTest.java
	•	Çakışan rezervasyon → 409 Conflict
	•	Olmayan rezervasyonla check-out → 409
	•	Eksik müşteri bilgisi → 400 Bad Request
