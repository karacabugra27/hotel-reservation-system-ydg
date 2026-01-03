package org.hotel.hotelreservationsystemydg.controller;

import org.hotel.hotelreservationsystemydg.dto.RoomResponseDto;
import org.hotel.hotelreservationsystemydg.service.RoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.format.annotation.DateTimeFormat;

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

    @GetMapping("/{id:\\d+}")
    ResponseEntity<RoomResponseDto> getRoomById(@PathVariable Long id) {
        RoomResponseDto response = roomService.getRoomById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/available")
    ResponseEntity<List<RoomResponseDto>> getAvailableRoomsForDates(
            @RequestParam("checkIn") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) java.time.LocalDate checkIn,
            @RequestParam("checkOut") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) java.time.LocalDate checkOut) {
        if (checkOut.isBefore(checkIn) || checkOut.equals(checkIn)) {
            throw new IllegalArgumentException("Cikis tarihi giris tarihinden sonra olmali");
        }
        List<RoomResponseDto> response = roomService.getAvailableRoomsForDates(checkIn, checkOut);
        return ResponseEntity.ok(response);
    }
}
