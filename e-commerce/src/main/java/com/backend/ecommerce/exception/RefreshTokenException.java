package com.backend.ecommerce.exception;

public class RefreshTokenException extends Exception {
    public RefreshTokenException(String refreshToken, String message) {
        super(refreshToken + " " + message);

    }
}
