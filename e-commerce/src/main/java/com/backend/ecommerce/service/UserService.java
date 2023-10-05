package com.backend.ecommerce.service;

import com.backend.ecommerce.api.dto.*;
import com.backend.ecommerce.exception.InvalidEmailOrPasswordException;
import com.backend.ecommerce.exception.RefreshTokenException;
import com.backend.ecommerce.exception.UserAlreadyExistsException;
import com.backend.ecommerce.model.LocalUser;
import com.backend.ecommerce.model.RefreshToken;
import com.backend.ecommerce.model.Role;
import com.backend.ecommerce.model.VerificationToken;
import com.backend.ecommerce.model.repository.RoleRepo;
import com.backend.ecommerce.model.repository.UserRepo;
import com.backend.ecommerce.model.repository.VerificationTokenRepo;
import com.backend.ecommerce.service.interfaces.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;

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
    public ResponseEntity<LoginResponse> loginUser(LoginRequest loginRequest) throws InvalidEmailOrPasswordException {
        try{
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

        }catch(Exception e){
            throw new InvalidEmailOrPasswordException("Invalid Email Or Password.");
        }
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
    public ResponseEntity refreshToken(HttpServletRequest request) throws RefreshTokenException {
        String refreshToken = jwtService.getJwtRefreshFromCookie(request);
        if(refreshToken != null && refreshToken.length() > 0){

            refreshTokenService.verifyExpiration(refreshToken);

            return refreshTokenService.findByToken(refreshToken)
                    .map(RefreshToken::getUser)
                    .map(localUser -> {
                        ResponseCookie jwtCookie = jwtService.generateJwtCookie(localUser);

                        return ResponseEntity.ok()
                                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                                .body(new SuccessMessage(
                                        HttpStatus.OK.value(),
                                        new Date(),
                                        "Token is refreshed successfully."));
                    }).get();

        }
        return ResponseEntity.
                badRequest().
                body(new ErrorMessage(
                        HttpStatus.BAD_REQUEST.value(),
                        new Date(),
                        "Refreshed token is empty"));
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
