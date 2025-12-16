package org.hotel.hotelreservationsystemydg.dto;

import java.math.BigDecimal;

public class RoomTypeCreateResponseDto {

    private Long roomTypeCreateId;
    private String name;
    private Integer capacity;
    private BigDecimal basePrice;

    public Long getRoomTypeCreateId() {
        return roomTypeCreateId;
    }

    public void setRoomTypeCreateId(Long roomTypeCreateId) {
        this.roomTypeCreateId = roomTypeCreateId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }
}
