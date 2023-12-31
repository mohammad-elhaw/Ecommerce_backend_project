package com.backend.ecommerce.exception;

import com.backend.ecommerce.api.dto.ErrorMessage;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleValidationException(MethodArgumentNotValidException ex){
        BindingResult bindingResult = ex.getBindingResult();

        List<String> errors = new ArrayList<>();
        for(FieldError fieldError : bindingResult.getFieldErrors()){
            String errorMessage = fieldError.getDefaultMessage();
            errors.add(errorMessage);
        }

        return new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                new Date(),
                errors.get(0)
        );
    }

    @ExceptionHandler(APIException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleAPIException(APIException ex){
        return new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                new Date(),
                ex.getMessage()
        );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage handleResourceNotFoundException(ResourceNotFoundException ex){
        return new ErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                new Date(),
                ex.getMessage()
        );
    }

    @ExceptionHandler(UploadImageFailureException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorMessage handleImageFailureException(UploadImageFailureException ex){
        return new ErrorMessage(
                HttpStatus.CONFLICT.value(),
                new Date(),
                ex.getMessage()
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage dataIntegrityException(DataIntegrityViolationException ex){
        return new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                new Date(),
                ex.getMessage()
        );
    }

    @ExceptionHandler(EmailNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleEmailNotFoundException(EmailNotFoundException ex){
        return new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                new Date(),
                ex.getMessage()
        );
    }

}
