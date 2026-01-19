package com.upb.agripos.exception;

/**
 * Exception untuk kegagalan autentikasi
 * Digunakan saat login gagal
 */
public class AuthenticationException extends Exception {

    public AuthenticationException() {
        super("Autentikasi gagal");
    }

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
