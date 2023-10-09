package com.backend.ecommerce.exception;

public class EmailNotFoundException extends Throwable {
    public EmailNotFoundException(String message) {
        super(message);
    }
}
