package org.hotel.hotelreservationsystemydg.repository;

import org.hotel.hotelreservationsystemydg.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    boolean existsByRoomIdAndCheckOutAfterAndCheckInBefore(
            Long roomId,
            LocalDate checkOutDate,
            LocalDate checkInDate
    );

    List<Reservation> findByCustomerId(Long customerId);

}
