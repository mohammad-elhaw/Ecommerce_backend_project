package com.backend.ecommerce.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class RatingResponse {

    private Long ratingId;
    private UserDTO user;
    private int rating;
    private LocalDateTime createdAt;
}
