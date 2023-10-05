package com.backend.ecommerce.exception;

import com.backend.ecommerce.api.dto.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;

@RestControllerAdvice
public class CustomControllerAdvice {

    @ExceptionHandler(value = RefreshTokenException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleRefreshTokenException(RefreshTokenException ex){
        return new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                new Date(),
                ex.getMessage()
        );
    }

    @ExceptionHandler(value = EmailTimeOutException.class)
    @ResponseStatus(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED)
    public ErrorMessage handleEmailTimeOutException(EmailTimeOutException ex){
        return new ErrorMessage(
                HttpStatus.BANDWIDTH_LIMIT_EXCEEDED.value(),
                new Date(),
                ex.getMessage()
        );
    }

}
