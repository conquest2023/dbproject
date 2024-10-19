package com.example.dbproject.excetpion;

public class RateLimitException extends Throwable {

    public RateLimitException(String message) {
        super(message);
    }
}
