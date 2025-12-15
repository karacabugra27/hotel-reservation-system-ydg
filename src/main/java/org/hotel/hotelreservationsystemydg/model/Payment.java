package org.hotel.hotelreservationsystemydg.model;

import jakarta.persistence.*;
import org.hotel.hotelreservationsystemydg.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Payment {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private Reservation reservation;

    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private LocalDateTime paymentDate;

}
