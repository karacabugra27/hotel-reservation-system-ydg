package org.hotel.hotelreservationsystemydg.dto;

import jakarta.validation.constraints.NotNull;

public class CheckOutRequestDto {

    @NotNull
    private Long reservationId;

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }
}
