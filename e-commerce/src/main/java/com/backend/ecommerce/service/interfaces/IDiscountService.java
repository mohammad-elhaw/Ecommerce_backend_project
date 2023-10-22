package com.backend.ecommerce.service.interfaces;

import com.backend.ecommerce.api.dto.CreateProductDTO;
import com.backend.ecommerce.model.Product;

public interface IDiscountService {
    void addDiscountForProduct(Product product, CreateProductDTO createProductDTO);
}
