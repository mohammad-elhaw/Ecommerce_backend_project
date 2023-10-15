package com.backend.ecommerce.service.interfaces;

import com.backend.ecommerce.api.dto.CreateProductDTO;
import com.backend.ecommerce.api.dto.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

public interface IProductService {
    void createProduct(CreateProductDTO product, Long categoryId);
    void deleteProduct(Long productId);
    ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    ProductResponse getProductsByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    ProductResponse getProductsByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    void updateProductImage(Long productId, MultipartFile file);
}
