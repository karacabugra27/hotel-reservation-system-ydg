package org.hotel.hotelreservationsystemydg.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Invoice {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private Reservation reservation;

    private BigDecimal price;
    private LocalDateTime invoiceDate;

}
