package com.backend.ecommerce.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordRequest {
    @NotBlank(message = "password should not be empty")
    @NotNull(message = "password should not be null")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[#$@!%&*?])[A-Za-z\\d#$@!%&*?]{8,}$"
            , message = "Password must have minimum eight characters, at least one letter, one number and one special character:")
    private String password;
    @NotBlank(message = "reset token should not be empty")
    @NotNull(message = "reset token should not be null")
    private String token;
}
