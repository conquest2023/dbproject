package com.example.dbproject.handler;

import com.example.dbproject.excetpion.ForbiddenException;
import com.example.dbproject.excetpion.RateLimitException;
import com.example.dbproject.excetpion.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {



    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleResourceNoyFoundException(ResourceNotFoundException e) {
        return e.getMessage();
    }

    @ExceptionHandler(RateLimitException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleRateLimitFoundException(RateLimitException e) {
        return e.getMessage();
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleForbiddenFoundException(ForbiddenException e) {
        return e.getMessage();
    }
}
