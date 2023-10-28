package com.backend.ecommerce.model.repository;

import com.backend.ecommerce.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepo extends JpaRepository<Rating, Long> {
    @Query("SELECT r FROM Rating r WHERE r.product.id=:productId")
    List<Rating> findAllByProductId(Long productId);
}
