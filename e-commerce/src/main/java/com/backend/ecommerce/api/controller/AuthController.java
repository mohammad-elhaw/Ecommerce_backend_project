package com.backend.ecommerce.api.controller;

import com.backend.ecommerce.api.dto.*;
import com.backend.ecommerce.exception.*;
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
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest){
        try{
            userService.createUser(registerRequest);
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
    public ResponseEntity<?> enableTheUser(@RequestParam("email") String email){
        try{
            userService.enableUser(email);
        } catch (EmailNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorMessage(
                            HttpStatus.BAD_REQUEST.value(),
                            new Date(),
                            ex.getMessage()
                    ));
        } catch (UserIsEnableException ex) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new SuccessMessage(
                            HttpStatus.OK.value(),
                            new Date(),
                            ex.getMessage()
                    ));
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        try{
            return userService.loginUser(loginRequest);
        } catch (InvalidEmailOrPasswordException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorMessage(
                            HttpStatus.BAD_REQUEST.value(),
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

    @PostMapping("/reset/create")
    public ResponseEntity<?> createResetPassword(@RequestParam("email") String email){
        try{
            userService.createResetPassword(email);
        } catch (EmailNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorMessage(
                            HttpStatus.BAD_REQUEST.value(),
                            new Date(),
                            "Your Email is invalid."
                    ));
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessMessage(
                        HttpStatus.OK.value(),
                        new Date(),
                        "Check your email."
                ));
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request){
        try{
            userService.resetPassword(request);
        } catch (InvalidResetTokenException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorMessage(
                            HttpStatus.BAD_REQUEST.value(),
                            new Date(),
                            ex.getMessage()
                    ));
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessMessage(
                        HttpStatus.OK.value(),
                        new Date(),
                        "You changed your password successfully"
                ));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(){
        try{
            userService.logoutUser();
        } catch (EmailNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorMessage(
                            HttpStatus.BAD_REQUEST.value(),
                            new Date(),
                            e.getMessage()
                    ));
        }

        return ResponseEntity.ok().body(new SuccessMessage(
                HttpStatus.OK.value(),
                new Date(),
                "You Logout Successfully"
        ));
    }
}
