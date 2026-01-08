package org.hotel.hotelreservationsystemydg.service;

import org.hotel.hotelreservationsystemydg.dto.CheckInRequestDto;
import org.hotel.hotelreservationsystemydg.dto.CheckOutRequestDto;
import org.hotel.hotelreservationsystemydg.dto.ReservationDateRangeDto;
import org.hotel.hotelreservationsystemydg.dto.ReservationRequestDto;
import org.hotel.hotelreservationsystemydg.dto.ReservationResponseDto;
import org.hotel.hotelreservationsystemydg.enums.PaymentStatus;
import org.hotel.hotelreservationsystemydg.enums.ReservationStatus;
import org.hotel.hotelreservationsystemydg.model.*;
import org.hotel.hotelreservationsystemydg.repository.CustomerRepository;
import org.hotel.hotelreservationsystemydg.repository.PaymentRepository;
import org.hotel.hotelreservationsystemydg.repository.ReservationRepository;
import org.hotel.hotelreservationsystemydg.repository.RoomRepository;
import org.hotel.hotelreservationsystemydg.service.impl.ReservationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
//ss
@ExtendWith(MockitoExtension.class)
public class ReservationServiceImplTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    private ReservationRequestDto createValidReservationRequest() {
        ReservationRequestDto dto = new ReservationRequestDto();
        dto.setCustomerId(1L);
        dto.setRoomId(1L);
        dto.setCheckInDate(LocalDate.now().plusDays(1));
        dto.setCheckOutDate(LocalDate.now().plusDays(3));
        return dto;
    }

    //t dolu oda rezerve edilememeli

    @Test
    void odaDoluysaRezervasyonYapilmamali() {
        ReservationRequestDto dto = createValidReservationRequest();

        when(reservationRepository.existsByRoomIdAndCheckOutAfterAndCheckInBefore(
                anyLong(), any(), any()
        )).thenReturn(true);

        IllegalStateException exception =
                assertThrows(IllegalStateException.class,
                        () -> reservationService.createReservation(dto));

        assertEquals("Belirtilen tarihlerde bu oda doludur!", exception.getMessage());

        verify(reservationRepository, never()).save(any());

    }

    //t oda bulunamazsa hata

    @Test
    void odaBulunamazsaHataVermeli() {
        ReservationRequestDto dto = createValidReservationRequest();

        when(reservationRepository.existsByRoomIdAndCheckOutAfterAndCheckInBefore(
                anyLong(), any(), any()
        )).thenReturn(false);

        when(roomRepository.findById(dto.getRoomId()))
                .thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class,
                () -> reservationService.createReservation(dto));
    }

    @Test
    void musteriBulunamazsaHataVermeli() {
        ReservationRequestDto dto = createValidReservationRequest();

        Room room = new Room();

        when(reservationRepository.existsByRoomIdAndCheckOutAfterAndCheckInBefore(
                anyLong(), any(), any()
        )).thenReturn(false);
        when(roomRepository.findById(dto.getRoomId()))
                .thenReturn(Optional.of(room));
        when(customerRepository.findById(dto.getCustomerId()))
                .thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class,
                () -> reservationService.createReservation(dto));

        verify(reservationRepository, never()).save(any());
    }


    //t başarılı rezervasyon

    @Test
    void basariliRezervasyon() {
        ReservationRequestDto dto = createValidReservationRequest();

        Room room = new Room();
        room.setRoomNumber("101");

        RoomType roomType = new RoomType();
        roomType.setName("Single");
        room.setRoomType(roomType);

        Customer customer = new Customer();
        customer.setFirstName("İsim");
        customer.setLastName("Soyisim");

        when(reservationRepository.existsByRoomIdAndCheckOutAfterAndCheckInBefore(
                anyLong(), any(), any()
        )).thenReturn(false);

        when(roomRepository.findById(dto.getRoomId()))
                .thenReturn(Optional.of(room));

        when(customerRepository.findById(dto.getCustomerId()))
                .thenReturn(Optional.of(customer));

        when(reservationRepository.save(any()))
                .thenAnswer(invocation -> {
                    Reservation r = invocation.getArgument(0);
                    r.setReservationStatus(ReservationStatus.CREATED);
                    return r;
                });

        ReservationResponseDto response = reservationService.createReservation(dto);

        assertNotNull(response);
        assertEquals("101", response.getRoomNumber());
        assertEquals("CREATED", response.getStatus());
        assertNotNull(response.getReservationCode());

        verify(reservationRepository).save(any());
    }

    @Test
    void musteriBilgileriEksikseHataVermeli() {
        ReservationRequestDto dto = createValidReservationRequest();
        dto.setCustomerId(null);
        dto.setFirstName(null);
        dto.setLastName("Soyad");
        dto.setPhone("5551231234");

        when(reservationRepository.existsByRoomIdAndCheckOutAfterAndCheckInBefore(
                anyLong(), any(), any()
        )).thenReturn(false);
        when(roomRepository.findById(dto.getRoomId()))
                .thenReturn(Optional.of(new Room()));

        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class,
                        () -> reservationService.createReservation(dto));

        assertEquals("Müşteri bilgileri zorunludur.", exception.getMessage());
        verify(customerRepository, never()).save(any());
    }

    @Test
    void musteriBilgileriIleRezervasyonOlusturulabilir() {
        ReservationRequestDto dto = createValidReservationRequest();
        dto.setCustomerId(null);
        dto.setFirstName("Test");
        dto.setLastName("User");
        dto.setPhone("5551231234");

        when(reservationRepository.existsByRoomIdAndCheckOutAfterAndCheckInBefore(
                anyLong(), any(), any()
        )).thenReturn(false);
        when(roomRepository.findById(dto.getRoomId()))
                .thenReturn(Optional.of(new Room()));
        when(customerRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ReservationResponseDto response = reservationService.createReservation(dto);

        assertNotNull(response);
        verify(customerRepository).save(any());
        verify(reservationRepository).save(any());
    }

    @Test
    void musteriIdVarkenYeniMusteriOlusturulmamali() {
        ReservationRequestDto dto = createValidReservationRequest();
        dto.setCustomerId(5L);

        when(reservationRepository.existsByRoomIdAndCheckOutAfterAndCheckInBefore(
                anyLong(), any(), any()
        )).thenReturn(false);
        when(roomRepository.findById(dto.getRoomId()))
                .thenReturn(Optional.of(new Room()));
        when(customerRepository.findById(dto.getCustomerId()))
                .thenReturn(Optional.of(new Customer()));
        when(reservationRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        reservationService.createReservation(dto);

        verify(customerRepository, never()).save(any());
        verify(reservationRepository).save(any());
    }

    @Test
    void rezervasyonKoduIleSorguBasarili() {
        Reservation reservation = new Reservation();
        reservation.setReservationCode("123456");

        when(reservationRepository.findByReservationCode("123456"))
                .thenReturn(Optional.of(reservation));

        ReservationResponseDto response = reservationService.getReservationByCode("123456");

        assertNotNull(response);
        assertEquals("123456", response.getReservationCode());
    }

    @Test
    void rezervasyonKoduBulunamazsaHataVermeli() {
        when(reservationRepository.findByReservationCode("000000"))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class,
                        () -> reservationService.getReservationByCode("000000"));

        assertEquals("Rezervasyon kodu bulunamadı.", exception.getMessage());
    }

    @Test
    void iptalEdilenRezervasyonlarDoluTarihListesineGirmemeli() {
        Reservation cancelled = new Reservation();
        cancelled.setReservationStatus(ReservationStatus.CANCELLED);

        Reservation active = new Reservation();
        active.setReservationStatus(ReservationStatus.CREATED);
        active.setCheckIn(LocalDate.now().plusDays(1));
        active.setCheckOut(LocalDate.now().plusDays(2));

        when(reservationRepository.findByRoom_Id(1L))
                .thenReturn(List.of(cancelled, active));

        List<ReservationDateRangeDto> response =
                reservationService.getReservationsByRoomId(1L);

        assertEquals(1, response.size());
        assertEquals(active.getCheckIn(), response.get(0).getCheckInDate());
        assertEquals(active.getCheckOut(), response.get(0).getCheckOutDate());
    }

    //t ödeme yapılmadan check-in

    @Test
    void odemeYapilmadanCheckInYapilamamali() {
        CheckInRequestDto dto = new CheckInRequestDto();
        dto.setReservationId(1L);

        Reservation reservation = new Reservation();
        reservation.setReservationStatus(ReservationStatus.CONFIRMED);

        //! rezervasyon var
        when(reservationRepository.findById(1L))
                .thenReturn(Optional.of(reservation));

        //! ödeme yok
        when(paymentRepository.findByReservationId(1L))
                .thenReturn(Optional.empty());

        //! hata mesajı kısmı
        assertThrows(IllegalStateException.class,
                () -> reservationService.checkIn(dto));
    }

    //t başarılı check-in

    @Test
    void odemeDurumuPaidDegilseCheckInYapilamaz() {
        CheckInRequestDto dto = new CheckInRequestDto();
        dto.setReservationId(1L);

        Reservation reservation = new Reservation();
        reservation.setReservationStatus(ReservationStatus.CONFIRMED);

        Payment payment = new Payment();
        payment.setPaymentStatus(PaymentStatus.PENDING);

        when(reservationRepository.findById(1L))
                .thenReturn(Optional.of(reservation));
        when(paymentRepository.findByReservationId(1L))
                .thenReturn(Optional.of(payment));

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> reservationService.checkIn(dto));

        assertEquals("ödeme tamamlanmamış", exception.getMessage());
        verify(reservationRepository, never()).save(any());
    }

    @Test
    void checkInRezervasyonBulunamazsaHataVermeli() {
        CheckInRequestDto dto = new CheckInRequestDto();
        dto.setReservationId(1L);

        when(reservationRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class,
                () -> reservationService.checkIn(dto));

        verify(paymentRepository, never()).findByReservationId(anyLong());
    }

   @Test
    void basariliCheckIn() {
        // arrange -> parametremiz
        CheckInRequestDto dto = new CheckInRequestDto();
        //checkin yapılacak rezervasyon
        dto.setReservationId(1L);
        //rezervasyon nesnesi
        Reservation reservation = new Reservation();
        reservation.setReservationStatus(ReservationStatus.CONFIRMED);
        //payment nesnesi
        Payment payment = new Payment();
        payment.setPaymentStatus(PaymentStatus.PAID);

        //! rezervasyon var
        when(reservationRepository.findById(1L))
                .thenReturn(Optional.of(reservation));
        //! ödeme yapılmış
        when(paymentRepository.findByReservationId(1L))
                .thenReturn(Optional.of(payment));

        // act -> asıl test kısmı (hazırlıkları deniyoruz)
        reservationService.checkIn(dto);
        // assert -> act sonrası status değişti mi?
        assertEquals(ReservationStatus.CHECKED_IN, reservation.getReservationStatus());
        // save gerçekten çağrıldı mı ?
        //! save çağrılıyor mu kontrolü
        verify(reservationRepository).save(reservation);



    }

    @Test
    void checkOutRezervasyonBulunamazsaHataVermeli() {
        CheckOutRequestDto dto = new CheckOutRequestDto();
        dto.setReservationId(1L);

        when(reservationRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class,
                () -> reservationService.checkOut(dto));

        verify(reservationRepository, never()).save(any());
    }

    @Test
    void basariliCheckOut() {
        CheckOutRequestDto dto = new CheckOutRequestDto();
        dto.setReservationId(1L);

        Reservation reservation = new Reservation();
        reservation.setReservationStatus(ReservationStatus.CHECKED_IN);

        when(reservationRepository.findById(1L))
                .thenReturn(Optional.of(reservation));

        reservationService.checkOut(dto);

        assertEquals(ReservationStatus.COMPLETED, reservation.getReservationStatus());
        verify(reservationRepository).save(reservation);
    }

}
