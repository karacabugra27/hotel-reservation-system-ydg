package org.hotel.hotelreservationsystemydg.controller;

import org.hotel.hotelreservationsystemydg.dto.PaymentRequestDto;
import org.hotel.hotelreservationsystemydg.dto.PaymentResponseDto;
import org.hotel.hotelreservationsystemydg.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentControllerTest {

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentController paymentController;

    private PaymentRequestDto buildRequest() {
        PaymentRequestDto dto = new PaymentRequestDto();
        dto.setReservationId(1L);
        dto.setPrice(BigDecimal.valueOf(300));
        return dto;
    }

    @Test
    void makePaymentBasariliOldugundaResponseOkDonmeli() {
        PaymentResponseDto responseDto = new PaymentResponseDto();
        responseDto.setPaymentId(11L);

        when(paymentService.makePayment(any()))
                .thenReturn(responseDto);

        PaymentRequestDto request = buildRequest();
        ResponseEntity<PaymentResponseDto> response = paymentController.makePayment(request);

        assertEquals(200, response.getStatusCode().value());
        assertSame(responseDto, response.getBody());
        verify(paymentService).makePayment(request);
    }
}
