package org.hotel.hotelreservationsystemydg.controller;

import jakarta.validation.Valid;
import org.hotel.hotelreservationsystemydg.dto.CheckInRequestDto;
import org.hotel.hotelreservationsystemydg.dto.CheckOutRequestDto;
import org.hotel.hotelreservationsystemydg.dto.ReservationRequestDto;
import org.hotel.hotelreservationsystemydg.dto.ReservationResponseDto;
import org.hotel.hotelreservationsystemydg.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/createReservation")
    ResponseEntity<ReservationResponseDto> createReservation(@Valid @RequestBody ReservationRequestDto dto) {

        ReservationResponseDto response = reservationService.createReservation(dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/check-in")
    ResponseEntity<Void> checkIn(@Valid @RequestBody CheckInRequestDto dto) {
        reservationService.checkIn(dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/check-out")
    ResponseEntity<Void> checkOut(@Valid @RequestBody CheckOutRequestDto dto) {
        reservationService.checkOut(dto);
        return ResponseEntity.ok().build();
    }


}
