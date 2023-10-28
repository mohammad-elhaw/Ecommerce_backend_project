package com.backend.ecommerce.service.interfaces;

import com.backend.ecommerce.api.dto.RatingRequest;
import com.backend.ecommerce.api.dto.RatingResponse;
import com.backend.ecommerce.model.LocalUser;

public interface IRatingService {
    RatingResponse createRating(RatingRequest request, LocalUser user);
    double calculateAverageRating(Long productId);
}
