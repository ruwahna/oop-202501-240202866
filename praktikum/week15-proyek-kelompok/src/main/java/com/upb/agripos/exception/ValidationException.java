package com.upb.agripos.exception;

import java.util.ArrayList;
import java.util.List;

public class ValidationException extends Exception {
    private String fieldName;
    private List<String> errors;

    public ValidationException(String message) {
        super(message);
        this.fieldName = "Unknown";
        this.errors = new ArrayList<>();
    }

    public ValidationException(String fieldName, String message) {
        super("Validation error on field \"" + fieldName + "\": " + message);
        this.fieldName = fieldName;
        this.errors = new ArrayList<>();
        this.errors.add(message);
    }

    public ValidationException(String fieldName, List<String> errors) {
        super("Validation errors on field \"" + fieldName + "\": " + String.join(", ", errors));
        this.fieldName = fieldName;
        this.errors = new ArrayList<>(errors);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
        this.fieldName = "Unknown";
        this.errors = new ArrayList<>();
    }

    public String getFieldName() {
        return fieldName;
    }

    public List<String> getErrors() {
        return new ArrayList<>(errors);
    }

    public void addError(String error) {
        this.errors.add(error);
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public int getErrorCount() {
        return errors.size();
    }
}