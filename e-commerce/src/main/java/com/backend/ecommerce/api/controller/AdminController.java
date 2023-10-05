package com.backend.ecommerce.api.controller;


import com.backend.ecommerce.api.dto.LoginRequest;
import com.backend.ecommerce.api.dto.ErrorMessage;
import com.backend.ecommerce.exception.InvalidEmailOrPasswordException;
import com.backend.ecommerce.service.interfaces.IAdminService;
import com.backend.ecommerce.service.interfaces.IUserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {

    private IAdminService adminService;
    private IUserService userService;

//    @PostMapping("/register")
//    public ResponseEntity<LocalUser> createAdmin(@Valid @RequestBody RegisterRequest registerRequest){
//
//        try{
//            LocalUser user = adminService.createAdmin(registerRequest);
//            return new ResponseEntity<>(user, HttpStatus.OK);
//        } catch (UserAlreadyExistsException e) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//
//    }

    @PostMapping("/login")
    public ResponseEntity<?> loginAdmin(@Valid @RequestBody LoginRequest loginRequest){
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
}
