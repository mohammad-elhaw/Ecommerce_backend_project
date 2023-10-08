package com.backend.ecommerce.service.interfaces;

import com.backend.ecommerce.api.dto.CreateProductRequest;

public interface IProductService {

    void createProduct(CreateProductRequest request);
}
