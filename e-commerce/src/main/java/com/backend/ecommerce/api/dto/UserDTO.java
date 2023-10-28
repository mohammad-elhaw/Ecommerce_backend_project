package com.backend.ecommerce.api.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDTO {

    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
}
