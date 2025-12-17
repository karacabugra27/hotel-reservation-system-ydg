package org.hotel.hotelreservationsystemydg.service;

import org.hotel.hotelreservationsystemydg.dto.PaymentRequestDto;
import org.hotel.hotelreservationsystemydg.dto.PaymentResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface PaymentService {

    PaymentResponseDto makePayment(PaymentRequestDto request);

}
