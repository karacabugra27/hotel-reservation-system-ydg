package org.hotel.hotelreservationsystemydg.service;

import org.hotel.hotelreservationsystemydg.dto.RoomResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RoomService {

    List<RoomResponseDto> getAvailableRooms();

}
