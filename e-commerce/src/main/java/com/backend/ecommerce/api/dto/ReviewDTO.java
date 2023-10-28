package com.backend.ecommerce.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReviewDTO {

    @NotBlank(message = "productId should not be empty")
    @NotNull(message = "productId should not be null")
    private Long productId;
    @NotBlank(message = "review should not be empty")
    @NotNull(message = "review should not be null")
    @Size(min = 10, message = "review should contain at least 10 characters.")
    private String review;

}
