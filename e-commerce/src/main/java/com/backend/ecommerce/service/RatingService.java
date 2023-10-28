package com.backend.ecommerce.service;

import com.backend.ecommerce.api.dto.RatingRequest;
import com.backend.ecommerce.api.dto.RatingResponse;
import com.backend.ecommerce.exception.APIException;
import com.backend.ecommerce.model.LocalUser;
import com.backend.ecommerce.model.Product;
import com.backend.ecommerce.model.Rating;
import com.backend.ecommerce.model.repository.ProductRepo;
import com.backend.ecommerce.model.repository.RatingRepo;
import com.backend.ecommerce.service.interfaces.IRatingService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RatingService implements IRatingService {

    private final ProductRepo productRepo;
    private final RatingRepo ratingRepo;
    private final ModelMapper mapper;

    @SneakyThrows
    @Override
    @Transactional
    public RatingResponse createRating(RatingRequest request, LocalUser user) {
        Product product = productRepo.findById(request.getProductId())
                .orElseThrow(()->new APIException("Product not found."));
        Rating rating = new Rating();
        rating.setRating(request.getRating());
        rating.setUser(user);
        rating.setProduct(product);
        rating.setCreatedAt(LocalDateTime.now());
        product.getRatings().add(rating);
        rating = ratingRepo.save(rating);

        return mapper.map(rating, RatingResponse.class);
    }

    @Override
    public double calculateAverageRating(Long productId) {
        List<Rating> ratings = ratingRepo.findAllByProductId(productId);
        int totalRatings = ratings.size();
        double sum = ratings.stream().mapToInt(Rating::getRating).sum();
        sum /= totalRatings;
        return sum;
    }
}
