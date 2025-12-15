package org.hotel.hotelreservationsystemydg.model;


import jakarta.persistence.*;

@Entity
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String number;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private Integer star;



}
