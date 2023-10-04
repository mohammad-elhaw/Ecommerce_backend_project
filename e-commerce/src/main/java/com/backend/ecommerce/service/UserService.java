package com.backend.ecommerce.service;

import com.backend.ecommerce.api.dto.LoginRequest;
import com.backend.ecommerce.api.dto.LoginResponse;
import com.backend.ecommerce.api.dto.RegisterRequest;
import com.backend.ecommerce.exception.UserAlreadyExistsException;
import com.backend.ecommerce.model.LocalUser;
import com.backend.ecommerce.model.RefreshToken;
import com.backend.ecommerce.model.Role;
import com.backend.ecommerce.model.VerificationToken;
import com.backend.ecommerce.model.repository.RoleRepo;
import com.backend.ecommerce.model.repository.UserRepo;
import com.backend.ecommerce.model.repository.VerificationTokenRepo;
import com.backend.ecommerce.service.interfaces.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;

@Service
@AllArgsConstructor
public class UserService implements IUserService {

    private UserRepo userRepo;
    private RoleRepo roleRepo;
    private VerificationTokenRepo verificationTokenRepo;
    private PasswordEncoder passwordEncoder;
    private JWTService jwtService;
    private AuthenticationProvider authenticationProvider;
    private RefreshTokenService refreshTokenService;


    @Override
    public ResponseEntity<LoginResponse> loginUser(LoginRequest loginRequest) {

        Authentication authentication =
                authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        MyCustomUserDetails userDetails = (MyCustomUserDetails) authentication.getPrincipal();

        ResponseCookie jwtCookie = jwtService.generateJwtCookie(userDetails.getUser());
        RefreshToken refreshToken = refreshTokenService.generateRefreshToken(userDetails.getUsername());
        ResponseCookie jwtRefreshCookie = jwtService.generateJwtRefreshCookie(refreshToken.getRefreshToken());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
                .body(new LoginResponse(userDetails.getUser().getUserId(), userDetails.getUsername(), userDetails.getUser().getRoles()));

    }

    @Override
    public LocalUser createUser(RegisterRequest registerRequest) throws UserAlreadyExistsException {

        if(userRepo.findByEmailIgnoreCase(registerRequest.getEmail()).isPresent()){
            throw new UserAlreadyExistsException("User Already Exists.");
        }

        Role userRole = roleRepo.findByRoleName("ROLE_USER");
        LocalUser user = new LocalUser();
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRoles(Arrays.asList(userRole));
        user.setCreatedAt(LocalDateTime.now());
        user.setPhoneNumber(registerRequest.getPhoneNumber());

        return userRepo.save(user);
    }

    @Override
    public void saveEmailToken(LocalUser user, String token) {
        VerificationToken verificationToken = new VerificationToken(user, token);
        verificationTokenRepo.save(verificationToken);
    }

    @Override
    public String verifyEmail(String token) {
        VerificationToken theToken = verificationTokenRepo.findByToken(token);
        if(theToken != null){
            LocalUser user = theToken.getUser();
            if(user.isEnabled()){
                return "this account has already verified, you can login.";
            }
            if(!jwtService.validateToken(token)){
                return "Token is expired.";
            }
            user.setEnabled(true);
            userRepo.save(user);
            return "now you can login.";
        }
        else{
            return "invalid verification token";
        }
    }
}
