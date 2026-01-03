package org.hotel.hotelreservationsystemydg.service;

import org.hotel.hotelreservationsystemydg.dto.RoomResponseDto;

import java.util.List;

public interface RoomService {

    List<RoomResponseDto> getAvailableRooms();

    RoomResponseDto getRoomById(Long id);

    List<RoomResponseDto> getAvailableRoomsForDates(java.time.LocalDate checkIn,
                                                    java.time.LocalDate checkOut);
}
