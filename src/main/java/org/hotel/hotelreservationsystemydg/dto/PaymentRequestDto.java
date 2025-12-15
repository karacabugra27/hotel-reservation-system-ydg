package org.hotel.hotelreservationsystemydg.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class PaymentRequestDto {

    //! request --> price
    //! request --> reservationId

    @NotNull(message = "reservation id zorunlu")
    private Long reservationId;

    @NotNull(message = "ödeme tutar zorunlu")
    @Positive(message = "ödeme tutarı pozitif olmalı")
    private BigDecimal price;

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
