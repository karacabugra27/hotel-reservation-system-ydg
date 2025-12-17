package org.hotel.hotelreservationsystemydg.controller;

import org.hotel.hotelreservationsystemydg.dto.CheckInRequestDto;
import org.hotel.hotelreservationsystemydg.dto.CheckOutRequestDto;
import org.hotel.hotelreservationsystemydg.dto.ReservationRequestDto;
import org.hotel.hotelreservationsystemydg.dto.ReservationResponseDto;
import org.hotel.hotelreservationsystemydg.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationControllerTest {

    @Mock
    private ReservationService reservationService;

    @InjectMocks
    private ReservationController reservationController;

    private ReservationRequestDto buildRequest() {
        ReservationRequestDto dto = new ReservationRequestDto();
        dto.setCustomerId(1L);
        dto.setRoomId(2L);
        dto.setCheckInDate(LocalDate.now().plusDays(1));
        dto.setCheckOutDate(LocalDate.now().plusDays(3));
        return dto;
    }

    @Test
    void createReservationBasariliOldugundaResponseDonmeli() {
        ReservationResponseDto responseDto = new ReservationResponseDto();
        responseDto.setReservationId(99L);

        when(reservationService.createReservation(any()))
                .thenReturn(responseDto);

        ResponseEntity<ReservationResponseDto> response =
                reservationController.createReservation(buildRequest());

        assertEquals(200, response.getStatusCode().value());
        assertSame(responseDto, response.getBody());

        verify(reservationService).createReservation(any());
    }

    @Test
    void checkInCagrisiServiseDelegasyonYapmali() {
        CheckInRequestDto dto = new CheckInRequestDto();
        dto.setReservationId(10L);

        ResponseEntity<Void> response = reservationController.checkIn(dto);

        assertEquals(200, response.getStatusCode().value());
        verify(reservationService).checkIn(dto);
    }

    @Test
    void checkOutCagrisiServiseDelegasyonYapmali() {
        CheckOutRequestDto dto = new CheckOutRequestDto();
        dto.setReservationId(10L);

        ResponseEntity<Void> response = reservationController.checkOut(dto);

        assertEquals(200, response.getStatusCode().value());
        verify(reservationService).checkOut(dto);
    }
}
