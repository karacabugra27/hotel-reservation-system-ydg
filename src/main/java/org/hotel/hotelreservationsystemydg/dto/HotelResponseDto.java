package org.hotel.hotelreservationsystemydg.dto;

public class HotelResponseDto {

    //! response --> hotelId
    //! response --> name
    //! response --> location
    //! response --> number
    //! response --> email
    //! response --> star

    private Long hotelId;
    private String name;
    private String location;
    private String number;
    private String email;
    private Integer star;

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getStar() {
        return star;
    }

    public void setStar(Integer star) {
        this.star = star;
    }
}
