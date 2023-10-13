package com.backend.ecommerce.service.interfaces;

import com.backend.ecommerce.api.dto.ProductDTO;
import com.backend.ecommerce.api.dto.ProductResponse;

public interface IProductService {
    void createProduct(ProductDTO product, Long categoryId);
    void deleteProduct(Long productId);
    ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    ProductResponse getProductsByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    ProductResponse getProductsByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
}
