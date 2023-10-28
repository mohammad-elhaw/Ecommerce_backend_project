package com.backend.ecommerce.model.repository;

import com.backend.ecommerce.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepo extends JpaRepository<Review, Long> {
    @Query("SELECT r FROM Review r WHERE r.product.id=:productId")
    List<Review> findProductReviews(Long productId);
    @Query("SELECT r FROM Review r WHERE (r.id=?1 AND r.product.id=?2 AND r.user.id=?3)")
    Optional<Review> findByReviewIdAndUserId(Long reviewId, Long productId , Long userId);
}
