package org.hotel.hotelreservationsystemydg.config;

import jakarta.annotation.PostConstruct;
import org.hotel.hotelreservationsystemydg.enums.RoomStatus;
import org.hotel.hotelreservationsystemydg.model.Room;
import org.hotel.hotelreservationsystemydg.model.RoomType;
import org.hotel.hotelreservationsystemydg.repository.RoomRepository;
import org.hotel.hotelreservationsystemydg.repository.RoomTypeRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Profile("selenium")
public class SeleniumDataInitializer {

    private final RoomTypeRepository roomTypeRepository;
    private final RoomRepository roomRepository;

    public SeleniumDataInitializer(RoomTypeRepository roomTypeRepository,
                                   RoomRepository roomRepository) {
        this.roomTypeRepository = roomTypeRepository;
        this.roomRepository = roomRepository;
    }

    @PostConstruct
    public void init() {

        if (roomRepository.count() > 0) {
            return; // tekrar tekrar eklemesin
        }

        RoomType standard = new RoomType();
        standard.setName("STANDARD");
        standard.setCapacity(2);
        standard.setBasePrice(BigDecimal.valueOf(1000));
        roomTypeRepository.save(standard);

        Room availableRoom = new Room();
        availableRoom.setRoomNumber("101");
        availableRoom.setRoomType(standard);
        availableRoom.setRoomStatus(RoomStatus.AVAILABLE);
        roomRepository.save(availableRoom);

        Room occupiedRoom = new Room();
        occupiedRoom.setRoomNumber("102");
        occupiedRoom.setRoomType(standard);
        occupiedRoom.setRoomStatus(RoomStatus.OCCUPIED);
        roomRepository.save(occupiedRoom);
    }
}