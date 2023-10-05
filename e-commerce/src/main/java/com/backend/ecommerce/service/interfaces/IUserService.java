package com.backend.ecommerce.service.interfaces;

import com.backend.ecommerce.api.dto.LoginRequest;
import com.backend.ecommerce.api.dto.LoginResponse;
import com.backend.ecommerce.api.dto.RegisterRequest;
import com.backend.ecommerce.exception.InvalidEmailOrPasswordException;
import com.backend.ecommerce.exception.RefreshTokenException;
import com.backend.ecommerce.exception.UserAlreadyExistsException;
import com.backend.ecommerce.model.LocalUser;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface IUserService {


    ResponseEntity<LoginResponse> loginUser(LoginRequest loginRequest) throws InvalidEmailOrPasswordException;
    LocalUser createUser(RegisterRequest registerRequest) throws UserAlreadyExistsException;
    void saveEmailToken(LocalUser user, String verificationToken);
    ResponseEntity refreshToken(HttpServletRequest request) throws RefreshTokenException;

    String verifyEmail(String token);
}
