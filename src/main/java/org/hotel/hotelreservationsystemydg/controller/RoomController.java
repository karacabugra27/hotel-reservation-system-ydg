package org.hotel.hotelreservationsystemydg.controller;

import org.hotel.hotelreservationsystemydg.dto.RoomResponseDto;
import org.hotel.hotelreservationsystemydg.service.RoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/room")
public class RoomController {
    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/getAvailableRooms")
    ResponseEntity<List<RoomResponseDto>> getAvailableRooms() {
        List<RoomResponseDto> response = roomService.getAvailableRooms();
        return ResponseEntity.ok(response);
    }
}
