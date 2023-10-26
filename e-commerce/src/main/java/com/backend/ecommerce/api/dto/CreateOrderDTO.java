package com.backend.ecommerce.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderDTO {

    @NotNull(message = "address must be exist")
    @NotBlank(message = "address must be exist")
    private String address;
    @NotNull(message = "city must be exist")
    @NotBlank(message = "city must be exist")
    private String city;
    @NotNull(message = "country must be exist")
    @NotBlank(message = "country must be exist")
    private String country;
    @NotNull(message = "street name must be exist")
    @NotBlank(message = "street name must be exist")
    private String streetName;
}
