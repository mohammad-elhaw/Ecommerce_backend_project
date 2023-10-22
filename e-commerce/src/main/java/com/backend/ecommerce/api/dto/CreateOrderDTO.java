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

    @NotNull
    @NotBlank
    private String address;
    @NotNull
    @NotBlank
    private String city;
    @NotNull
    @NotBlank
    private String country;
}
