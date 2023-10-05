package com.backend.ecommerce.exception;

public class EmailTimeOutException extends Throwable {
    public EmailTimeOutException(String message) {
        super(message);
    }
}
