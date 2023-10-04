package com.backend.ecommerce.service.interfaces;

import com.backend.ecommerce.api.dto.LoginRequest;
import com.backend.ecommerce.api.dto.LoginResponse;
import com.backend.ecommerce.api.dto.RegisterRequest;
import com.backend.ecommerce.exception.UserAlreadyExistsException;
import com.backend.ecommerce.model.LocalUser;
import org.springframework.http.ResponseEntity;

import javax.security.auth.login.CredentialException;

public interface IUserService {


    public ResponseEntity<LoginResponse> loginUser(LoginRequest loginRequest) throws CredentialException;
    public LocalUser createUser(RegisterRequest registerRequest) throws UserAlreadyExistsException;
    void saveEmailToken(LocalUser user, String verificationToken);

    String verifyEmail(String token);
}
