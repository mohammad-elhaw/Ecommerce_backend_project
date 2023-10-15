package com.backend.ecommerce.api.dto;

import com.backend.ecommerce.model.Inventory;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductResponseDTO {
    private Long productId;
    private String productName;
    private String shortDescription;
    private String longDescription;
    private Double price;
    private String imageUrl;
    private double discountPrice;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Inventory inventory;
}
