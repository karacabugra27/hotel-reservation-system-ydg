package org.hotel.hotelreservationsystemydg.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class HotelRequestDto {
    //! request --> name
    //! request --> location
    //! request --> number
    //! request --> email
    //! request --> star

    @NotBlank
    private String name;

    @NotBlank
    private String location;

    @NotBlank
    private String number;

    @NotBlank
    private String email;

    @Min(1)
    @Max(5)
    private Integer star;

}
