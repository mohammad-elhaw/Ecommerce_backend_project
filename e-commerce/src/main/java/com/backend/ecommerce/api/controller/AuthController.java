package com.backend.ecommerce.api.controller;

import com.backend.ecommerce.api.dto.ErrorMessage;
import com.backend.ecommerce.api.dto.LoginRequest;
import com.backend.ecommerce.api.dto.RegisterRequest;
import com.backend.ecommerce.api.dto.SuccessMessage;
import com.backend.ecommerce.exception.InvalidEmailOrPasswordException;
import com.backend.ecommerce.exception.RefreshTokenException;
import com.backend.ecommerce.exception.UserAlreadyExistsException;
import com.backend.ecommerce.exception.UserIsNotEnableException;
import com.backend.ecommerce.service.interfaces.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private IUserService userService;


    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest, final HttpServletRequest request){
        try{
            userService.createUser(registerRequest, request);
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

    @PostMapping("/verifyEmail")
    public ResponseEntity<?> verifyEmail(@RequestParam("token") String token){
        return userService.verifyEmail(token);
    }

    @PostMapping("/activateAccount")
    public ResponseEntity<?> enableTheUser(@RequestParam("email") String email, final HttpServletRequest request){
        userService.enableUser(email, request);
        return new ResponseEntity<>(HttpStatus.OK);
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
        } catch (UserIsNotEnableException e) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(new ErrorMessage(
                            HttpStatus.FORBIDDEN.value(),
                            new Date(),
                            e.getMessage()
                    ));
        }
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) throws RefreshTokenException {
        return userService.refreshToken(request);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(){
        userService.logoutUser();
        return ResponseEntity.ok().body(new SuccessMessage(
                HttpStatus.OK.value(),
                new Date(),
                "You Logout Successfully"
        ));
    }
}
