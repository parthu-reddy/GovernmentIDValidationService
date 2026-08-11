package com.fooddelivery.governmentid.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_GATEWAY)
public class ExternalVerificationException extends RuntimeException {
    public ExternalVerificationException(String message) {
        super(message);
    }

    public ExternalVerificationException(String message, Throwable cause) {
        super(message, cause);
    }
}
