package org.hotel.hotelreservationsystemydg.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentResponseDto {

    //! response --> paymentDate
    //! response --> paymentStatus
    //! response --> price
    //! response --> paymentId

    private Long paymentId;
    private BigDecimal price;
    private String status;
    private LocalDateTime paymentDate;

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }
}
