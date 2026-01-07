package org.hotel.hotelreservationsystemydg.controller;

import jakarta.validation.Valid;
import org.hotel.hotelreservationsystemydg.dto.CheckInRequestDto;
import org.hotel.hotelreservationsystemydg.dto.CheckOutRequestDto;
import org.hotel.hotelreservationsystemydg.dto.ReservationDateRangeDto;
import org.hotel.hotelreservationsystemydg.dto.ReservationRequestDto;
import org.hotel.hotelreservationsystemydg.dto.ReservationResponseDto;
import org.hotel.hotelreservationsystemydg.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @GetMapping
    ResponseEntity<List<ReservationResponseDto>> getReservations() {
        return ResponseEntity.ok(reservationService.getReservations());
    }

    @GetMapping("/room/{roomId}")
    ResponseEntity<List<ReservationDateRangeDto>> getReservationsByRoomId(@PathVariable Long roomId) {
        return ResponseEntity.ok(reservationService.getReservationsByRoomId(roomId));
    }

    @GetMapping("/code/{reservationCode}")
    ResponseEntity<ReservationResponseDto> getReservationByCode(@PathVariable String reservationCode) {
        return ResponseEntity.ok(reservationService.getReservationByCode(reservationCode));
    }

    @PostMapping("/cancel/{reservationId}")
    ResponseEntity<ReservationResponseDto> cancelReservation(@PathVariable Long reservationId) {
        ReservationResponseDto response = reservationService.cancelReservation(reservationId);
        return ResponseEntity.ok(response);
    }


}
