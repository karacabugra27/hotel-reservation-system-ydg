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

            roomTypeRepository.save(single);
            roomTypeRepository.save(doubleRoom);
        }

        if (roomRepository.count() == 0) {
            RoomType single = roomTypeRepository.findAll().get(0);

            Room room101 = new Room();
            room101.setRoomNumber("101");
            room101.setRoomType(single);
            room101.setRoomStatus(RoomStatus.AVAILABLE);

            Room room102 = new Room();
            room102.setRoomNumber("102");
            room102.setRoomType(single);
            room102.setRoomStatus(RoomStatus.AVAILABLE);

            roomRepository.save(room101);
            roomRepository.save(room102);
        }

        if (customerRepository.count() == 0) {
            Customer demo = new Customer();
            demo.setFirstName("Demo");
            demo.setLastName("Student");
            demo.setNumber("5551234567");
            customerRepository.save(demo);
        }
    }
}
