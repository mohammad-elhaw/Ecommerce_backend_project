package com.backend.ecommerce.api.controller;

import com.backend.ecommerce.api.dto.LoginRequest;
import com.backend.ecommerce.api.dto.LoginResponse;
import com.backend.ecommerce.api.dto.RegisterRequest;
import com.backend.ecommerce.event.RegistrationCompleteEvent;
import com.backend.ecommerce.exception.RefreshTokenException;
import com.backend.ecommerce.exception.UserAlreadyExistsException;
import com.backend.ecommerce.model.LocalUser;
import com.backend.ecommerce.model.RefreshToken;
import com.backend.ecommerce.service.JWTService;
import com.backend.ecommerce.service.RefreshTokenService;
import com.backend.ecommerce.service.interfaces.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.CredentialException;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private IUserService userService;
    private ApplicationEventPublisher publisher;
    private JWTService jwtService;
    private RefreshTokenService refreshTokenService;


    @PostMapping("/register")
    public ResponseEntity register(@Valid @RequestBody RegisterRequest registerRequest, final HttpServletRequest request){
        try{
            LocalUser user = userService.createUser(registerRequest);
            publisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(request)));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (UserAlreadyExistsException e) {
            return new ResponseEntity(HttpStatus.CONFLICT);
        }
    }

    private String applicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

    @GetMapping("/verifyEmail")
    public String verifyEmail(@RequestParam("token") String token){
        return userService.verifyEmail(token);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest){
        try{
            return userService.loginUser(loginRequest);
        } catch (CredentialException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/refreshToken")
    public ResponseEntity refreshToken(HttpServletRequest request){
        String refreshToken = jwtService.getJwtRefreshFromCookie(request);

        if(refreshToken != null){
            try{
                RefreshToken token = refreshTokenService.findByToken(refreshToken).get();
                refreshTokenService.verifyExpiration(token);
            } catch (RefreshTokenException e) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }


            return refreshTokenService.findByToken(refreshToken)
                    .map(RefreshToken::getUser)
                    .map(localUser -> {
                        ResponseCookie jwtCookie = jwtService.generateJwtCookie(localUser);

                        return ResponseEntity.ok()
                                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                                .body("Token is refreshed successfully.");

                    }).get();
        }
        return ResponseEntity.badRequest().body("Refreshed token is empty");
    }
}
