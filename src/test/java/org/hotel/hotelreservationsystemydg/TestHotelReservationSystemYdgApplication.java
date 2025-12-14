package org.hotel.hotelreservationsystemydg;

import org.springframework.boot.SpringApplication;

public class TestHotelReservationSystemYdgApplication {

    public static void main(String[] args) {
        SpringApplication.from(HotelReservationSystemYdgApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
