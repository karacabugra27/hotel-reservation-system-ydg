package org.hotel.hotelreservationsystemydg.repository;

import org.hotel.hotelreservationsystemydg.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
