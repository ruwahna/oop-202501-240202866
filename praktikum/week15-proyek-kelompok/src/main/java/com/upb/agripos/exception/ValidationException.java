package com.upb.agripos.exception;

/**
 * Exception untuk validasi input yang tidak valid
 * Digunakan di Service layer untuk validasi bisnis
 */
public class ValidationException extends Exception {

    public ValidationException() {
        super();
    }

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidationException(Throwable cause) {
        super(cause);
    }
}
