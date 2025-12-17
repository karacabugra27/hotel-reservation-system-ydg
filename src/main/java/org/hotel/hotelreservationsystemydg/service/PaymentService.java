package org.hotel.hotelreservationsystemydg.service;

import org.hotel.hotelreservationsystemydg.dto.PaymentRequestDto;
import org.hotel.hotelreservationsystemydg.dto.PaymentResponseDto;

public interface PaymentService {

    PaymentResponseDto makePayment(PaymentRequestDto request);

}
