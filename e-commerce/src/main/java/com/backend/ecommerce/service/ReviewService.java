package com.backend.ecommerce.service;

import com.backend.ecommerce.api.dto.ReviewDTO;
import com.backend.ecommerce.api.dto.ReviewResponseDTO;
import com.backend.ecommerce.api.dto.UserDTO;
import com.backend.ecommerce.exception.APIException;
import com.backend.ecommerce.exception.ResourceNotFoundException;
import com.backend.ecommerce.model.LocalUser;
import com.backend.ecommerce.model.Product;
import com.backend.ecommerce.model.Review;
import com.backend.ecommerce.model.repository.ProductRepo;
import com.backend.ecommerce.model.repository.ReviewRepo;
import com.backend.ecommerce.service.interfaces.IReviewService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService implements IReviewService {

    private final ProductRepo productRepo;
    private final ReviewRepo reviewRepo;
    private final ModelMapper mapper;
    @Override
    @Transactional
    public void createReview(LocalUser user, ReviewDTO reviewDTO) {

        Product product = productRepo.findById(reviewDTO.getProductId())
                .orElseThrow(()-> new ResourceNotFoundException("Product", "productId", reviewDTO.getProductId()));
        Review review = new Review();
        review.setReview(reviewDTO.getReview());
        review.setUser(user);
        review.setProduct(product);
        review.setCreatedAt(LocalDateTime.now());
        product.getReviews().add(review);
        productRepo.save(product);
    }

    @SneakyThrows
    @Override
    public List<ReviewResponseDTO> getProductReviews(Long productId) {
        List<Review> reviews = reviewRepo.findProductReviews(productId);
        if(reviews.size() == 0){
            throw new APIException("There are no reviews for the product.");
        }
        return reviews.stream()
                .map(review -> {
                    ReviewResponseDTO reviewResponseDTO = mapper.map(review, ReviewResponseDTO.class);
                    reviewResponseDTO.setUser(mapper.map(review.getUser(), UserDTO.class));
                    return reviewResponseDTO;
                }).toList();
    }

    @Override
    @Transactional
    public ReviewResponseDTO updateReview(LocalUser user, Long reviewId, ReviewDTO request) {
        Review review = reviewRepo.findByReviewIdAndUserId(reviewId, request.getProductId(), user.getUserId())
                .orElseThrow(()-> new ResourceNotFoundException("Review", "reviewId", reviewId));
        review.setReview(request.getReview());
        reviewRepo.save(review);
        ReviewResponseDTO reviewResponseDTO = mapper.map(review, ReviewResponseDTO.class);
        reviewResponseDTO.setUser(mapper.map(review.getUser(), UserDTO.class));
        return reviewResponseDTO;
    }

    @Override
    @Transactional
    public void deleteReview(LocalUser user, Long reviewId, Long productId) {
        Review review = reviewRepo.findByReviewIdAndUserId(reviewId, productId, user.getUserId())
                .orElseThrow(()-> new ResourceNotFoundException("Review", "reviewId", reviewId));
        reviewRepo.delete(review);
    }

}
