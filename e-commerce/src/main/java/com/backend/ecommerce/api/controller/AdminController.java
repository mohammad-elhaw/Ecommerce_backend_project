package com.backend.ecommerce.api.controller;


import com.backend.ecommerce.api.dto.RegisterRequest;
import com.backend.ecommerce.exception.UserAlreadyExistsException;
import com.backend.ecommerce.model.LocalUser;
import com.backend.ecommerce.service.interfaces.IAdminService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {

    private IAdminService adminService;

    @PostMapping("/register")
    public ResponseEntity<LocalUser> createAdmin(@Valid @RequestBody RegisterRequest registerRequest){

        try{
            LocalUser user = adminService.createAdmin(registerRequest);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (UserAlreadyExistsException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }
}
