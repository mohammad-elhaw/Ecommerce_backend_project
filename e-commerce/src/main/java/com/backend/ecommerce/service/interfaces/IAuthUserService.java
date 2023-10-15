package com.backend.ecommerce.service.interfaces;

import com.backend.ecommerce.api.dto.LoginRequest;
import com.backend.ecommerce.api.dto.LoginResponse;
import com.backend.ecommerce.api.dto.RegisterRequest;
import com.backend.ecommerce.api.dto.ResetPasswordRequest;
import com.backend.ecommerce.exception.*;
import com.backend.ecommerce.model.LocalUser;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface IAuthUserService {
    ResponseEntity<LoginResponse> loginUser(LoginRequest loginRequest) throws InvalidEmailOrPasswordException, UserIsNotEnableException;
    void createUser(RegisterRequest registerRequest) throws UserAlreadyExistsException;
    void saveEmailToken(LocalUser user, String verificationToken);
    ResponseEntity<?> refreshToken(HttpServletRequest request) throws RefreshTokenException;
    ResponseEntity<?> verifyEmail(String token);
    void enableUser(String email);
    void logoutUser();
    void createResetPassword(String email);
    void saveResetToken(LocalUser user, String resetToken);
    void resetPassword(ResetPasswordRequest request) throws InvalidResetTokenException;
}
