package org.hotel.hotelreservationsystemydg.model;

import jakarta.persistence.*;
import org.hotel.hotelreservationsystemydg.enums.RoomStatus;

@Entity
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String roomNumber;

    @ManyToOne
    private RoomType roomType;

    @Enumerated(EnumType.STRING)
    private RoomStatus roomStatus;

}
