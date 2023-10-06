package com.backend.ecommerce.exception;

import com.backend.ecommerce.api.dto.LoginResponse;
import org.springframework.http.ResponseEntity;

public class UserIsNotEnableException extends Throwable {
    public UserIsNotEnableException(String message) {
        super(message);
    }
}
