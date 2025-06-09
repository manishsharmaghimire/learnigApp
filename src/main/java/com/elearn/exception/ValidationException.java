package com.elearn.exception;

import org.springframework.validation.Errors;

/**
 * Exception thrown when validation fails.
 * Contains validation errors that can be used to provide detailed error messages.
 */
public class ValidationException extends RuntimeException {
    private final Errors errors;

    public ValidationException(String message, Errors errors) {
        super(message);
        this.errors = errors;
    }

    public Errors getErrors() {
        return errors;
    }
}
