package org.hotel.hotelreservationsystemydg.service.impl;

import org.hotel.hotelreservationsystemydg.dto.PaymentRequestDto;
import org.hotel.hotelreservationsystemydg.dto.PaymentResponseDto;
import org.hotel.hotelreservationsystemydg.enums.PaymentStatus;
import org.hotel.hotelreservationsystemydg.model.Payment;
import org.hotel.hotelreservationsystemydg.model.Reservation;
import org.hotel.hotelreservationsystemydg.repository.PaymentRepository;
import org.hotel.hotelreservationsystemydg.repository.ReservationRepository;
import org.hotel.hotelreservationsystemydg.service.PaymentService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository, ReservationRepository reservationRepository) {
        this.paymentRepository = paymentRepository;
        this.reservationRepository = reservationRepository;
    }

    @Override
    public PaymentResponseDto makePayment(PaymentRequestDto dto) {

        Reservation reservation = reservationRepository.findById(dto.getReservationId())
                .orElseThrow(() -> new IllegalStateException("rezervasyon bulunamadı"));

        Payment payment = new Payment();
        payment.setPrice(dto.getPrice());
        payment.setPaymentDate(LocalDateTime.now());
        payment.setReservation(reservation);
        payment.setPaymentStatus(PaymentStatus.PAID);

        paymentRepository.save(payment);

        PaymentResponseDto response = new PaymentResponseDto();
        response.setPaymentId(payment.getId());
        response.setPaymentDate(payment.getPaymentDate());
        response.setPrice(payment.getPrice());
        response.setStatus(payment.getPaymentStatus().name());

        return response;
    }
}
