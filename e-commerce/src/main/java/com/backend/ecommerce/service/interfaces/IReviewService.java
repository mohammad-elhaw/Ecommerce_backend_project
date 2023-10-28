package com.backend.ecommerce.service.interfaces;

import com.backend.ecommerce.api.dto.ReviewDTO;
import com.backend.ecommerce.api.dto.ReviewResponseDTO;
import com.backend.ecommerce.model.LocalUser;

import java.util.List;

public interface IReviewService {
    void createReview(LocalUser user, ReviewDTO reviewDTO);
    List<ReviewResponseDTO> getProductReviews(Long productId);
    ReviewResponseDTO updateReview(LocalUser user, Long reviewId, ReviewDTO request);
    void deleteReview(LocalUser user, Long reviewId, Long productId);
}
