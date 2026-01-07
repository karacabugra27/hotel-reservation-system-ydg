package org.hotel.hotelreservationsystemydg.util;

import org.hotel.hotelreservationsystemydg.enums.RoomStatus;
import org.hotel.hotelreservationsystemydg.model.Customer;
import org.hotel.hotelreservationsystemydg.model.Role;
import org.hotel.hotelreservationsystemydg.model.Room;
import org.hotel.hotelreservationsystemydg.model.RoomType;
import org.hotel.hotelreservationsystemydg.repository.CustomerRepository;
import org.hotel.hotelreservationsystemydg.repository.RoleRepository;
import org.hotel.hotelreservationsystemydg.repository.RoomRepository;
import org.hotel.hotelreservationsystemydg.repository.RoomTypeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final RoomRepository roomRepository;
    private final CustomerRepository customerRepository;

    public DataInitializer(RoleRepository roleRepository,
                           RoomTypeRepository roomTypeRepository,
                           RoomRepository roomRepository,
                           CustomerRepository customerRepository) {
        this.roleRepository = roleRepository;
        this.roomTypeRepository = roomTypeRepository;
        this.roomRepository = roomRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public void run(String... args) {

        if (roleRepository.count() == 0) {
            Role admin = new Role();
            admin.setName("ADMIN");

            Role receptionist = new Role();
            receptionist.setName("RECEPTIONIST");

            roleRepository.save(admin);
            roleRepository.save(receptionist);
        }

        if (roomTypeRepository.count() == 0) {
            RoomType single = new RoomType();
            single.setName("Single");
            single.setCapacity(1);
            single.setBasePrice(BigDecimal.valueOf(100));

            RoomType doubleRoom = new RoomType();
            doubleRoom.setName("Double");
            doubleRoom.setCapacity(2);
            doubleRoom.setBasePrice(BigDecimal.valueOf(180));

            RoomType twin = new RoomType();
            twin.setName("Twin");
            twin.setCapacity(2);
            twin.setBasePrice(BigDecimal.valueOf(200));

            RoomType family = new RoomType();
            family.setName("Family");
            family.setCapacity(4);
            family.setBasePrice(BigDecimal.valueOf(320));

            RoomType deluxe = new RoomType();
            deluxe.setName("Deluxe");
            deluxe.setCapacity(3);
            deluxe.setBasePrice(BigDecimal.valueOf(420));

            roomTypeRepository.save(single);
            roomTypeRepository.save(doubleRoom);
            roomTypeRepository.save(twin);
            roomTypeRepository.save(family);
            roomTypeRepository.save(deluxe);
        }

        if (roomRepository.count() == 0) {
            RoomType single = roomTypeRepository.findByName("Single").orElseThrow();
            RoomType doubleRoom = roomTypeRepository.findByName("Double").orElseThrow();
            RoomType twin = roomTypeRepository.findByName("Twin").orElseThrow();
            RoomType family = roomTypeRepository.findByName("Family").orElseThrow();
            RoomType deluxe = roomTypeRepository.findByName("Deluxe").orElseThrow();

            Room room101 = new Room();
            room101.setRoomNumber("101");
            room101.setRoomType(single);
            room101.setRoomStatus(RoomStatus.AVAILABLE);

            Room room102 = new Room();
            room102.setRoomNumber("102");
            room102.setRoomType(single);
            room102.setRoomStatus(RoomStatus.AVAILABLE);

            Room room201 = new Room();
            room201.setRoomNumber("201");
            room201.setRoomType(doubleRoom);
            room201.setRoomStatus(RoomStatus.AVAILABLE);

            Room room202 = new Room();
            room202.setRoomNumber("202");
            room202.setRoomType(doubleRoom);
            room202.setRoomStatus(RoomStatus.OCCUPIED);

            Room room301 = new Room();
            room301.setRoomNumber("301");
            room301.setRoomType(twin);
            room301.setRoomStatus(RoomStatus.AVAILABLE);

            Room room401 = new Room();
            room401.setRoomNumber("401");
            room401.setRoomType(family);
            room401.setRoomStatus(RoomStatus.AVAILABLE);

            Room room501 = new Room();
            room501.setRoomNumber("501");
            room501.setRoomType(deluxe);
            room501.setRoomStatus(RoomStatus.AVAILABLE);

            roomRepository.save(room101);
            roomRepository.save(room102);
            roomRepository.save(room201);
            roomRepository.save(room202);
            roomRepository.save(room301);
            roomRepository.save(room401);
            roomRepository.save(room501);
        }

        if (customerRepository.count() == 0) {
            Customer demo = new Customer();
            demo.setFirstName("Demo");
            demo.setLastName("Student");
            demo.setNumber("5551234567");
            customerRepository.save(demo);

            Customer testUser = new Customer();
            testUser.setFirstName("Test");
            testUser.setLastName("User");
            testUser.setNumber("5552345678");
            customerRepository.save(testUser);

            Customer guest = new Customer();
            guest.setFirstName("Guest");
            guest.setLastName("Sample");
            guest.setNumber("5553456789");
            customerRepository.save(guest);
        }
    }
}
