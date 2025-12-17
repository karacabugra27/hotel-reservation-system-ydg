package org.hotel.hotelreservationsystemydg.service;

import org.hotel.hotelreservationsystemydg.dto.RoomResponseDto;
import org.hotel.hotelreservationsystemydg.enums.RoomStatus;
import org.hotel.hotelreservationsystemydg.model.Room;
import org.hotel.hotelreservationsystemydg.model.RoomType;
import org.hotel.hotelreservationsystemydg.repository.RoomRepository;
import org.hotel.hotelreservationsystemydg.repository.RoomTypeRepository;
import org.hotel.hotelreservationsystemydg.service.impl.RoomServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoomServiceImplTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private RoomTypeRepository roomTypeRepository;

    @InjectMocks
    private RoomServiceImpl roomService;

    @Test
    void musaitOdalarRoomResponseDtoOlarakDonmeli() {
        RoomType roomType = new RoomType();
        roomType.setName("Single");

        Room room = new Room();
        room.setId(10L);
        room.setRoomNumber("101");
        room.setRoomType(roomType);
        room.setRoomStatus(RoomStatus.AVAILABLE);

        when(roomRepository.findByRoomStatus(RoomStatus.AVAILABLE))
                .thenReturn(List.of(room));

        List<RoomResponseDto> result = roomService.getAvailableRooms();

        assertEquals(1, result.size());
        RoomResponseDto dto = result.get(0);
        assertEquals(10L, dto.getId());
        assertEquals("101", dto.getRoomNumber());
        assertEquals("Single", dto.getRoomTypeName());
        assertEquals("AVAILABLE", dto.getStatus());
        assertNotNull(dto);

        verify(roomRepository).findByRoomStatus(RoomStatus.AVAILABLE);
    }
}
