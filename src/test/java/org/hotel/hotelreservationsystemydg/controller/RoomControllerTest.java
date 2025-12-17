package org.hotel.hotelreservationsystemydg.controller;

import org.hotel.hotelreservationsystemydg.dto.RoomResponseDto;
import org.hotel.hotelreservationsystemydg.service.RoomService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoomControllerTest {

    @Mock
    private RoomService roomService;

    @InjectMocks
    private RoomController roomController;

    @Test
    void getAvailableRoomsServisiCagiripListeDonmeli() {
        RoomResponseDto dto = new RoomResponseDto();
        dto.setId(1L);

        when(roomService.getAvailableRooms())
                .thenReturn(List.of(dto));

        ResponseEntity<List<RoomResponseDto>> response = roomController.getAvailableRooms();

        assertEquals(200, response.getStatusCode().value());
        assertSame(dto, response.getBody().get(0));
        verify(roomService).getAvailableRooms();
    }
}
