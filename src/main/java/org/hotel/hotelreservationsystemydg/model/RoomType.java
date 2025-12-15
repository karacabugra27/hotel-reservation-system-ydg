package org.hotel.hotelreservationsystemydg.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class RoomType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer capacity;

    @Column(nullable = false)
    private BigDecimal basePrice;

}
