package com.upb.agripos.exception;

public class AuthenticationException extends Exception {
    private String username;
    private String reason;

    public AuthenticationException(String message) {
        super(message);
        this.username = "Unknown";
        this.reason = message;
    }

    public AuthenticationException(String username, String reason) {
        super("Authentication failed for user: " + username + ". Reason: " + reason);
        this.username = username;
        this.reason = reason;
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
        this.username = "Unknown";
        this.reason = message;
    }

    public String getUsername() {
        return username;
    }

    public String getReason() {
        return reason;
    }

    public boolean isInvalidCredentials() {
        return reason != null && reason.toLowerCase().contains("invalid");
    }
}