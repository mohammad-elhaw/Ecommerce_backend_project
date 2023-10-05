package com.backend.ecommerce.api.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RegisterRequest {

    @NotBlank
    @NotNull
    @Email
    private String email;
    @NotBlank
    @NotNull
    @Size(max = 10, min = 3)
    private String firstName;
    @NotBlank
    @NotNull
    @Size(max = 10, min = 3)
    private String lastName;

    @NotNull
    @NotBlank
    private String phoneNumber;

    @NotBlank
    @NotNull
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[#$@!%&*?])[A-Za-z\\d#$@!%&*?]{8,}$")
    private String password;

}
