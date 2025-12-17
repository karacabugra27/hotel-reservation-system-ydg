package org.hotel.hotelreservationsystemydg.service;

import org.hotel.hotelreservationsystemydg.dto.PaymentRequestDto;
import org.hotel.hotelreservationsystemydg.dto.PaymentResponseDto;
import org.hotel.hotelreservationsystemydg.model.Payment;
import org.hotel.hotelreservationsystemydg.model.Reservation;
import org.hotel.hotelreservationsystemydg.repository.PaymentRepository;
import org.hotel.hotelreservationsystemydg.repository.ReservationRepository;
import org.hotel.hotelreservationsystemydg.service.impl.PaymentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private PaymentRequestDto buildRequest() {
        PaymentRequestDto dto = new PaymentRequestDto();
        dto.setReservationId(1L);
        dto.setPrice(BigDecimal.valueOf(250));
        return dto;
    }

    @Test
    void rezervasyonBulunamazsaHataFirlatmali() {
        PaymentRequestDto request = buildRequest();

        when(reservationRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class,
                () -> paymentService.makePayment(request));

        verify(paymentRepository, never()).save(any());
    }

    @Test
    void basariliOdemeKaydiYapilmali() {
        PaymentRequestDto request = buildRequest();

        Reservation reservation = new Reservation();
        reservation.setId(1L);

        when(reservationRepository.findById(1L))
                .thenReturn(Optional.of(reservation));
        when(paymentRepository.save(any()))
                .thenAnswer(invocation -> {
                    Payment payment = invocation.getArgument(0);
                    payment.setId(5L);
                    return payment;
                });

        PaymentResponseDto response = paymentService.makePayment(request);

        assertNotNull(response);
        assertEquals(5L, response.getPaymentId());
        assertEquals(request.getPrice(), response.getPrice());
        assertEquals("PAID", response.getStatus());
        assertNotNull(response.getPaymentDate());

        ArgumentCaptor<Payment> paymentCaptor = ArgumentCaptor.forClass(Payment.class);
        verify(paymentRepository).save(paymentCaptor.capture());
        Payment saved = paymentCaptor.getValue();

        assertEquals(reservation, saved.getReservation());
        assertEquals(request.getPrice(), saved.getPrice());
        assertNotNull(saved.getPaymentDate());
    }
}
