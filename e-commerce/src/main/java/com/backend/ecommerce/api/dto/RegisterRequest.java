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

    @NotBlank(message = "Email should not be empty")
    @NotNull(message = "Email should not be null")
    @Email(message = "Invalid Email")
    private String email;
    @NotBlank(message = "first name should not be empty")
    @NotNull(message = "first name should not be null")
    @Size(max = 10, min = 3, message = "Invalid Name: Must be consist of 3 - 10 character")
    private String firstName;
    @NotBlank(message = "Last name should not be empty")
    @NotNull(message = "Last name should not be null")
    @Size(max = 10, min = 3, message = "Invalid Name: Must be consist of 3 - 10 character" )
    private String lastName;
    @NotNull(message = "phone number should not be null")
    @NotBlank(message = "phone number should not be empty")
    private String phoneNumber;
    @NotBlank(message = "password should not be empty")
    @NotNull(message = "password should not be null")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[#$@!%&*?])[A-Za-z\\d#$@!%&*?]{8,}$"
            , message = "Password must have minimum eight characters, at least one letter, one number and one special character:")
    private String password;

}
