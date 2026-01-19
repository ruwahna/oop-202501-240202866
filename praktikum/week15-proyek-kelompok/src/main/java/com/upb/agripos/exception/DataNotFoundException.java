package com.upb.agripos.exception;

/**
 * Exception untuk data yang tidak ditemukan di database
 */
public class DataNotFoundException extends Exception {

    public DataNotFoundException() {
        super("Data tidak ditemukan");
    }

    public DataNotFoundException(String message) {
        super(message);
    }

    public DataNotFoundException(String entityType, String identifier) {
        super(String.format("%s dengan identifier '%s' tidak ditemukan", entityType, identifier));
    }
}
