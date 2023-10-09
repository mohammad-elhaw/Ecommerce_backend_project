package com.backend.ecommerce.exception;

public class InvalidResetTokenException extends Throwable {
    public InvalidResetTokenException(String message) {
        super(message);
    }
}
