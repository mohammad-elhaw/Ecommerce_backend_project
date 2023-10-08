package com.backend.ecommerce.api.dto;

import com.backend.ecommerce.model.Size;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductRequest {

    @NotNull(message = "name should not be null.")
    @NotBlank(message = "name should not be empty.")
    private String name;
    @NotNull(message = "short description should not be null.")
    @NotBlank(message = "short description should not be empty.")
    private String shortDescription;
    @NotNull(message = "Long description should not be null.")
    @NotBlank(message = "Long description should not be empty.")
    private String longDescription;
    @NotNull(message = "price should not be empty.")
    private Double price;
    @NotNull(message = "you must insert the image.")
    @NotBlank(message = "you must insert the image.")
    private String imageUrl;
    private Double discountPrice;
    private Double discountPercent;
    @JsonProperty("sizes")
    private Set<Size> sizes = new HashSet<>();
    @NotNull(message = "quantity should not be empty.")
    private Integer quantity;
    @NotNull(message = "you must insert first level category.")
    @NotBlank(message = "you must insert first level category.")
    private String firstLevelCategory;
    @NotNull(message = "you must insert second level category.")
    @NotBlank(message = "you must insert second level category.")
    private String secondLevelCategory;
}
