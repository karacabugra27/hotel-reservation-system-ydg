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

    @NotNull(message = "customer id zorunlu")
    private Long customerId;

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
