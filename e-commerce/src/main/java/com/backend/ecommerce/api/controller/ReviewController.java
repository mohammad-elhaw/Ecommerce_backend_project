package com.backend.ecommerce.api.controller;

import com.backend.ecommerce.api.dto.ReviewDTO;
import com.backend.ecommerce.api.dto.ReviewResponseDTO;
import com.backend.ecommerce.model.LocalUser;
import com.backend.ecommerce.service.interfaces.IReviewService;
import com.backend.ecommerce.service.interfaces.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class ReviewController {

    private final IUserService userService;
    private final IReviewService reviewService;

    @PostMapping("/user/review")
    public ResponseEntity<?> createReviewHandler(@RequestBody ReviewDTO review){
        LocalUser user = userService.getAuthenticatedUser();
        reviewService.createReview(user, review);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/user/review/{reviewId}")
    public ResponseEntity<?> updateReviewHandler(@RequestBody ReviewDTO request ,@PathVariable Long reviewId){
        LocalUser user = userService.getAuthenticatedUser();
        ReviewResponseDTO response = reviewService.updateReview(user, reviewId, request);
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/user/review/{reviewId}/product/{productId}")
    public ResponseEntity<?> deleteReviewHandler(@PathVariable Long reviewId, @PathVariable Long productId){
        LocalUser user = userService.getAuthenticatedUser();
        reviewService.deleteReview(user, reviewId, productId);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping("/public/review/{productId}")
    public ResponseEntity<?> getProductReviewsHandler(@PathVariable Long productId){
        List<ReviewResponseDTO> response = reviewService.getProductReviews(productId);
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

}
