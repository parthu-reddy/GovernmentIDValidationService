package com.fooddelivery.governmentid.exception;

public class ExternalVerificationException extends RuntimeException {
    public ExternalVerificationException(String message) {
        super(message);
    }

    public ExternalVerificationException(String message, Throwable cause) {
        super(message, cause);
    }
}
