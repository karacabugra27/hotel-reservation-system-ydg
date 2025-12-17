package org.hotel.hotelreservationsystemydg.controller;

import jakarta.validation.Valid;
import org.hotel.hotelreservationsystemydg.dto.PaymentRequestDto;
import org.hotel.hotelreservationsystemydg.dto.PaymentResponseDto;
import org.hotel.hotelreservationsystemydg.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/make-payment")
    ResponseEntity<PaymentResponseDto> makePayment(@Valid @RequestBody PaymentRequestDto dto){
        PaymentResponseDto response = paymentService.makePayment(dto);
        return ResponseEntity.ok(response);
    }

}
