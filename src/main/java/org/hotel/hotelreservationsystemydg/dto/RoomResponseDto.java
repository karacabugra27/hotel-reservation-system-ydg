package org.hotel.hotelreservationsystemydg.dto;

public class RoomResponseDto {

    private Long id;
    private String roomTypeName;
    private String roomNumber;
    private String status;
    private Integer capacity;
    private java.math.BigDecimal basePrice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoomTypeName() {
        return roomTypeName;
    }

    public void setRoomTypeName(String roomTypeName) {
        this.roomTypeName = roomTypeName;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public java.math.BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(java.math.BigDecimal basePrice) {
        this.basePrice = basePrice;
    }
}
