package com.backend.ecommerce.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RatingRequest {

    @NotNull(message = "product id must be exist")
    @NotBlank(message = "product id must be exist")
    private Long productId;
    @NotNull(message = "rating must be exist")
    private Integer rating;

}
