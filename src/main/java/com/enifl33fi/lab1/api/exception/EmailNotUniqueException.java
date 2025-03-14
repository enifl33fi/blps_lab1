package com.enifl33fi.lab1.api.exception;

public class EmailNotUniqueException extends RuntimeException {
    public EmailNotUniqueException(String email) {
        super(String.format("Email '%s' already exists", email));
    }
}
