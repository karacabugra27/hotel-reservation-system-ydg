package org.hotel.hotelreservationsystemydg.service.impl;

import org.hotel.hotelreservationsystemydg.dto.RoomResponseDto;
import org.hotel.hotelreservationsystemydg.enums.RoomStatus;
import org.hotel.hotelreservationsystemydg.repository.RoomRepository;
import org.hotel.hotelreservationsystemydg.repository.RoomTypeRepository;
import org.hotel.hotelreservationsystemydg.service.RoomService;

import java.util.List;

public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final RoomTypeRepository roomTypeRepository;

    public RoomServiceImpl(RoomRepository roomRepository, RoomTypeRepository roomTypeRepository) {
        this.roomRepository = roomRepository;
        this.roomTypeRepository = roomTypeRepository;
    }

    @Override
    public List<RoomResponseDto> getAvailableRooms() {

        return roomRepository.findByStatus(RoomStatus.AVAILABLE)
                .stream()
                .map(room -> {
                    RoomResponseDto dto = new RoomResponseDto();
                    dto.setId(room.getId());
                    dto.setRoomNumber(room.getRoomNumber());
                    dto.setRoomTypeName(room.getRoomType().getName());
                    dto.setStatus(room.getRoomStatus().name());
                    return dto;
                })
                .toList();
    }
}
