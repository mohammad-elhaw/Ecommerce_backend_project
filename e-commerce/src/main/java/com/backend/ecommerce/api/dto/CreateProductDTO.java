package com.backend.ecommerce.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateProductDTO {

    @NotNull(message = "you must insert product name")
    @NotBlank(message = "you must insert product name")
    @Size(min = 5, message = "product name must contain at least 5 characters")
    private String productName;

    @NotNull(message = "you must insert short description")
    @NotBlank(message = "you must insert short description")
    @Size(min = 10, message = "short description must contain at least 10 characters")
    private String shortDescription;

    @NotNull(message = "you must insert long description")
    @NotBlank(message = "you must insert long description")
    @Size(min = 10, message = "long description must contain at least 10 characters")
    private String longDescription;

    @NotNull(message = "you must insert the price")
    private Double price;
    private double discountPercent;

    @NotNull(message = "you must insert the quantity.")
    private Integer quantity;

}
