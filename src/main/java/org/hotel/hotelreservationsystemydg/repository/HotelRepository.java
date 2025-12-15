package org.hotel.hotelreservationsystemydg.repository;

import org.hotel.hotelreservationsystemydg.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
}
