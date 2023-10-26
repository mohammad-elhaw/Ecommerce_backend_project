package com.backend.ecommerce.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShippedAddressDTO {

    private Long addressId;
    private String address;
    private String streetName;
    private String city;
    private String country;

}
