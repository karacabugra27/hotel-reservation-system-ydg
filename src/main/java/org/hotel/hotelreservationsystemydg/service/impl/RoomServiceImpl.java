package org.hotel.hotelreservationsystemydg.service.impl;

import org.hotel.hotelreservationsystemydg.dto.RoomResponseDto;
import org.hotel.hotelreservationsystemydg.enums.RoomStatus;
import org.hotel.hotelreservationsystemydg.model.Room;
import org.hotel.hotelreservationsystemydg.repository.ReservationRepository;
import org.hotel.hotelreservationsystemydg.repository.RoomRepository;
import org.hotel.hotelreservationsystemydg.service.RoomService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;

    public RoomServiceImpl(RoomRepository roomRepository, ReservationRepository reservationRepository) {
        this.roomRepository = roomRepository;
        this.reservationRepository = reservationRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomResponseDto> getAvailableRooms() {

        return roomRepository.findByRoomStatus(RoomStatus.AVAILABLE)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public RoomResponseDto getRoomById(Long id) {
        return roomRepository.findWithRoomTypeById(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new IllegalStateException("Bu oda kaydi yok"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomResponseDto> getAvailableRoomsForDates(java.time.LocalDate checkIn,
                                                           java.time.LocalDate checkOut) {
        return roomRepository.findByRoomStatus(RoomStatus.AVAILABLE)
                .stream()
                .filter(room -> !reservationRepository.existsByRoomIdAndCheckOutAfterAndCheckInBefore(
                        room.getId(), checkOut, checkIn))
                .map(this::mapToDto)
                .toList();
    }

    private RoomResponseDto mapToDto(Room room) {
        RoomResponseDto dto = new RoomResponseDto();
        dto.setId(room.getId());
        dto.setRoomNumber(room.getRoomNumber());
        if (room.getRoomType() != null) {
            dto.setRoomTypeName(room.getRoomType().getName());
            dto.setCapacity(room.getRoomType().getCapacity());
            dto.setBasePrice(room.getRoomType().getBasePrice());
        }
        if (room.getRoomStatus() != null) {
            dto.setStatus(room.getRoomStatus().name());
        }
        return dto;
    }
}
