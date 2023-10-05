package com.backend.ecommerce.exception;

public class InvalidEmailOrPasswordException extends Throwable{

    public InvalidEmailOrPasswordException(String message) {
        super(message);
    }
}
