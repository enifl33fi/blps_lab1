package com.enifl33fi.lab1.api.exception;

public class EmailOtpException extends RuntimeException {
    public EmailOtpException(String message) {
        super(String.format("Failed: %s", message));
    }
}
