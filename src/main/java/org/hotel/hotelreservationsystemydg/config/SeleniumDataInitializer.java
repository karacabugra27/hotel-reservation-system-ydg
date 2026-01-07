package org.hotel.hotelreservationsystemydg.config;

import jakarta.annotation.PostConstruct;
import org.hotel.hotelreservationsystemydg.enums.RoomStatus;
import org.hotel.hotelreservationsystemydg.model.Customer;
import org.hotel.hotelreservationsystemydg.model.Room;
import org.hotel.hotelreservationsystemydg.model.RoomType;
import org.hotel.hotelreservationsystemydg.repository.CustomerRepository;
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
    private final CustomerRepository customerRepository;

    public SeleniumDataInitializer(RoomTypeRepository roomTypeRepository,
                                   RoomRepository roomRepository,
                                   CustomerRepository customerRepository) {
        this.roomTypeRepository = roomTypeRepository;
        this.roomRepository = roomRepository;
        this.customerRepository = customerRepository;
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

        RoomType deluxe = new RoomType();
        deluxe.setName("DELUXE");
        deluxe.setCapacity(3);
        deluxe.setBasePrice(BigDecimal.valueOf(1600));
        roomTypeRepository.save(deluxe);

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

        Room availableRoomDeluxe = new Room();
        availableRoomDeluxe.setRoomNumber("201");
        availableRoomDeluxe.setRoomType(deluxe);
        availableRoomDeluxe.setRoomStatus(RoomStatus.AVAILABLE);
        roomRepository.save(availableRoomDeluxe);

        if (customerRepository.count() == 0) {
            Customer demo = new Customer();
            demo.setFirstName("Selenium");
            demo.setLastName("Student");
            demo.setNumber("5550000000");
            customerRepository.save(demo);

            Customer testUser = new Customer();
            testUser.setFirstName("Selenium");
            testUser.setLastName("User");
            testUser.setNumber("5551111111");
            customerRepository.save(testUser);
        }
    }
}
