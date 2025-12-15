package org.hotel.hotelreservationsystemydg.model;

import jakarta.persistence.*;
import org.hotel.hotelreservationsystemydg.enums.ReservationStatus;

import java.time.LocalDate;

@Entity
public class Reservation {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private Room room;

    private LocalDate checkIn;
    private LocalDate checkOut;

    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus;
}
