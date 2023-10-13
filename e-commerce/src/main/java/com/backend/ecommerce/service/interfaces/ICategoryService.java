package com.backend.ecommerce.service.interfaces;

import com.backend.ecommerce.api.dto.CategoryResponse;
import com.backend.ecommerce.model.Category;

public interface ICategoryService {
    void createCategory(Category category);
    void updateCategory(Category category, Long categoryId);
    void deleteCategory(Long categoryId);
    CategoryResponse getCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
}
