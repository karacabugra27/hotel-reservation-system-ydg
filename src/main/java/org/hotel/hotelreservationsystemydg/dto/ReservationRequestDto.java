package org.hotel.hotelreservationsystemydg.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class ReservationRequestDto {

    //! request --> müşteri id
    //! request --> room id
    //! request --> check-in
    //! request --> check-out

    private Long customerId;

    private String firstName;

    private String lastName;

    private String phone;

    @NotNull
    private Long roomId;

    @NotNull(message = "check-in tarihi zorunlu")
    @FutureOrPresent(message = "check-in tarihi bugünden önce olamaz")
    private LocalDate checkInDate;

    @NotNull(message = "check-out tarihi zorunlu")
    @Future(message = "check-out tarihi gelecekte olmalı")
    private LocalDate checkOutDate;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }
}
