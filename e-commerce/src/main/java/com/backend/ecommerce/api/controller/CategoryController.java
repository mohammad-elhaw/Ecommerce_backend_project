package com.backend.ecommerce.api.controller;

import com.backend.ecommerce.api.config.AppConstants;
import com.backend.ecommerce.api.dto.CategoryResponse;
import com.backend.ecommerce.model.Category;
import com.backend.ecommerce.service.interfaces.ICategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class CategoryController {

    private final ICategoryService categoryService;

    @PostMapping("/admin/category")
    public ResponseEntity<?> createCategory(@Valid @RequestBody Category category){
        categoryService.createCategory(category);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/admin/category/{categoryId}")
    public ResponseEntity<?> updateCategory(@RequestBody Category category, @PathVariable Long categoryId){
        categoryService.updateCategory(category, categoryId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/public/categories")
    public ResponseEntity<CategoryResponse> getCategories(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_CATEGORIES_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder){

        CategoryResponse categoryResponse = categoryService.getCategories(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(categoryResponse, HttpStatus.FOUND);
    }

    @DeleteMapping("/admin/category/{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long categoryId){
        categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
