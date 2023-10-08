package com.backend.ecommerce.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoginRequest {

    @NotBlank(message = "Email should not be empty")
    @NotNull(message = "Email should not be null")
    private String email;
    @NotBlank(message = "password should not be empty")
    @NotNull(message = "password should not be null")
    private String password;

}
