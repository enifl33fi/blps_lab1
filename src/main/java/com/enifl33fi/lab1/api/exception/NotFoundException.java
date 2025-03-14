package com.enifl33fi.lab1.api.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(String.format("%s not found", message));
    }
}
