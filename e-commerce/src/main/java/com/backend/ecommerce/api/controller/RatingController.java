package com.backend.ecommerce.api.controller;

import com.backend.ecommerce.api.dto.RatingRequest;
import com.backend.ecommerce.api.dto.RatingResponse;
import com.backend.ecommerce.model.LocalUser;
import com.backend.ecommerce.service.interfaces.IRatingService;
import com.backend.ecommerce.service.interfaces.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class RatingController {

    private final IUserService userService;
    private final IRatingService ratingService;
    @PostMapping("/user/rating")
    public ResponseEntity<?> createRatingHandler(@RequestBody RatingRequest request){
        LocalUser user = userService.getAuthenticatedUser();
        RatingResponse response = ratingService.createRating(request, user);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/public/rating/average/product/{productId}")
    public ResponseEntity<?> getAverageRatingHandler(@PathVariable Long productId){
        double response = ratingService.calculateAverageRating(productId);
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

}
