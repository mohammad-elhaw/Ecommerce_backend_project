package com.backend.ecommerce.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CartDTO {

    private Long cartId;
    private Double totalPrice = 0.0;
    private List<ProductResponseDTO> products = new ArrayList<>();
}
