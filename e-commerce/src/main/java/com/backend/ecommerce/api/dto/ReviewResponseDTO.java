package com.backend.ecommerce.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class ReviewResponseDTO {

    private Long reviewId;
    private String review;
    private UserDTO user;
    private LocalDateTime createdAt;

}
