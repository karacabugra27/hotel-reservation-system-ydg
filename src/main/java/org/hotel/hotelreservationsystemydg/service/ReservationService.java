package org.hotel.hotelreservationsystemydg.service;

import org.hotel.hotelreservationsystemydg.dto.CheckInRequestDto;
import org.hotel.hotelreservationsystemydg.dto.CheckOutRequestDto;
import org.hotel.hotelreservationsystemydg.dto.ReservationRequestDto;
import org.hotel.hotelreservationsystemydg.dto.ReservationResponseDto;

import java.util.List;

public interface ReservationService {

    ReservationResponseDto createReservation(ReservationRequestDto request);

    void checkIn(CheckInRequestDto request);

    void checkOut(CheckOutRequestDto request);

    List<ReservationResponseDto> getReservations();

    ReservationResponseDto cancelReservation(Long reservationId);

}
