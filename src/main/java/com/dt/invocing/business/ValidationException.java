package com.dt.invocing.business;

public class ValidationException extends RuntimeException {

    public ValidationException(String message, Throwable t) {
        super(message, t);
    }

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException() {
    }

}
