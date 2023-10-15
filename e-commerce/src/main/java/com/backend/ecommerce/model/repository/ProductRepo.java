package com.backend.ecommerce.model.repository;

import com.backend.ecommerce.model.Category;
import com.backend.ecommerce.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {
    Page<Product> findByCategory(Category category, Pageable pageable);
    Page<Product> findByProductNameStartingWithIgnoreCase(String keyword, Pageable pageable);
}
