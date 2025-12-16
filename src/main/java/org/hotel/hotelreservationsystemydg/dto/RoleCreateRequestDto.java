package org.hotel.hotelreservationsystemydg.dto;

import jakarta.validation.constraints.NotBlank;

public class RoleCreateRequestDto {

    @NotBlank
    private String name; //! ADMIN, RECEPTIONIST

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
