package org.hotel.hotelreservationsystemydg.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.hotel.hotelreservationsystemydg.enums.RoomStatus;
import org.hotel.hotelreservationsystemydg.integration.config.TestSecurityConfig;
import org.hotel.hotelreservationsystemydg.model.Room;
import org.hotel.hotelreservationsystemydg.model.RoomType;
import org.hotel.hotelreservationsystemydg.repository.RoomRepository;
import org.hotel.hotelreservationsystemydg.repository.RoomTypeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
@Transactional
@Rollback
class RoomAvailabilityIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoomTypeRepository roomTypeRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final List<Room> createdRooms = new ArrayList<>();

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        roomRepository.deleteAll();

        if (roomTypeRepository.count() == 0) {
            RoomType roomType = new RoomType();
            roomType.setName("TEST_TYPE");
            roomType.setCapacity(2);
            roomType.setBasePrice(BigDecimal.valueOf(100));
            roomTypeRepository.save(roomType);
        }
    }



    @AfterEach
    void tearDown() {
        roomRepository.deleteAll(createdRooms);
        createdRooms.clear();
    }

    @Test
    void sadeceMusaitOdalarDoner() throws Exception {
        Room availableRoom = createRoom("501A", RoomStatus.AVAILABLE);
        Room occupiedRoom = createRoom("502B", RoomStatus.OCCUPIED);

        String responseBody = mockMvc.perform(get("/room/getAvailableRooms")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<Map<String, Object>> rooms = objectMapper.readValue(responseBody, new TypeReference<>() {});

        Optional<Map<String, Object>> available = rooms.stream()
                .filter(room -> availableRoom.getRoomNumber().equals(room.get("roomNumber")))
                .findFirst();

        assertThat(available).isPresent();
        assertThat(available.get().get("status")).isEqualTo("AVAILABLE");

        boolean occupiedPresent = rooms.stream()
                .anyMatch(room -> occupiedRoom.getRoomNumber().equals(room.get("roomNumber")));

        assertThat(occupiedPresent).isFalse();
    }

    private Room createRoom(String roomNumber, RoomStatus status) {
        RoomType roomType = roomTypeRepository.findAll().get(0);
        Room room = new Room();
        room.setRoomNumber(roomNumber);
        room.setRoomType(roomType);
        room.setRoomStatus(status);
        Room saved = roomRepository.save(room);
        createdRooms.add(saved);
        return saved;
    }
}
