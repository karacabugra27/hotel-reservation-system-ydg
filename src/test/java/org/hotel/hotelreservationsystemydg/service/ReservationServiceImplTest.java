package org.hotel.hotelreservationsystemydg.service;

import org.hotel.hotelreservationsystemydg.dto.CheckInRequestDto;
import org.hotel.hotelreservationsystemydg.dto.ReservationRequestDto;
import org.hotel.hotelreservationsystemydg.dto.ReservationResponseDto;
import org.hotel.hotelreservationsystemydg.enums.PaymentStatus;
import org.hotel.hotelreservationsystemydg.enums.ReservationStatus;
import org.hotel.hotelreservationsystemydg.model.Customer;
import org.hotel.hotelreservationsystemydg.model.Payment;
import org.hotel.hotelreservationsystemydg.model.Reservation;
import org.hotel.hotelreservationsystemydg.model.Room;
import org.hotel.hotelreservationsystemydg.repository.CustomerRepository;
import org.hotel.hotelreservationsystemydg.repository.PaymentRepository;
import org.hotel.hotelreservationsystemydg.repository.ReservationRepository;
import org.hotel.hotelreservationsystemydg.repository.RoomRepository;
import org.hotel.hotelreservationsystemydg.service.impl.ReservationServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

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

        when(reservationRepository.existsByRoom_IdAndCheckOutAfterAndCheckInBefore(
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

        when(reservationRepository.existsByRoom_IdAndCheckOutAfterAndCheckInBefore(
                anyLong(), any(), any()
        )).thenReturn(false);

        when(roomRepository.findById(dto.getRoomId()))
                .thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class,
                () -> reservationService.createReservation(dto));
    }


    //t başarılı rezervasyon

    void basariliRezervasyon() {
        ReservationRequestDto dto = createValidReservationRequest();

        Room room = new Room();
        room.setRoomNumber("101");

        Customer customer = new Customer();
        customer.setFirstName("İsim");
        customer.setLastName("Soyisim");

        when(reservationRepository.existsByRoom_IdAndCheckOutAfterAndCheckInBefore(
                anyLong(), any(), any()
        )).thenReturn(true);

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
        assertEquals("CREATEAD", response.getStatus());

        verify(reservationRepository).save(any());
    }

    //t ödeme yapılmadan check-in

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

}
