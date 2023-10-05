package com.backend.ecommerce.api.controller;

import com.backend.ecommerce.api.dto.LoginRequest;
import com.backend.ecommerce.api.dto.RegisterRequest;
import com.backend.ecommerce.event.RegistrationCompleteEvent;
import com.backend.ecommerce.api.dto.ErrorMessage;
import com.backend.ecommerce.exception.InvalidEmailOrPasswordException;
import com.backend.ecommerce.exception.RefreshTokenException;
import com.backend.ecommerce.exception.UserAlreadyExistsException;
import com.backend.ecommerce.model.LocalUser;
import com.backend.ecommerce.service.JWTService;
import com.backend.ecommerce.service.MyCustomUserDetails;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

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
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new ErrorMessage(
                            HttpStatus.CONFLICT.value(),
                            new Date(),
                            "The Email is Already Exists"
                    ));
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
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        try{
            return userService.loginUser(loginRequest);
        } catch (InvalidEmailOrPasswordException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorMessage(
                            HttpStatus.UNAUTHORIZED.value(),
                            new Date(),
                            e.getMessage()
                    ));
        }
    }

    @GetMapping("/test")
    public ResponseEntity test(){
        return ResponseEntity.ok().body("hello man");
    }

    @PostMapping("/refreshToken")
    public ResponseEntity refreshToken(HttpServletRequest request) throws RefreshTokenException {
        return userService.refreshToken(request);
    }

    @PostMapping("/logout")
    public ResponseEntity logout(){
        Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(!principle.toString().equals("anonymousUser")){
            LocalUser user = ((MyCustomUserDetails)principle).getUser();
            refreshTokenService.deleteByUser(user);
        }
        ResponseCookie jwtCookie = jwtService.getCleanJwtCookie();
        ResponseCookie jwtRefreshCookie = jwtService.getCleanRefreshCookie();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
                .body("You 've been sign out successfully!");
    }
}
