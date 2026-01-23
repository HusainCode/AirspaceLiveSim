package com.flighttracker.common.exception;

public class BadRequestException extends RuntimeException {

    private final String field;

    public BadRequestException(String message) {
        super(message);
        this.field = null;
    }

    public BadRequestException(String field, String message) {
        super(message);
        this.field = field;
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
        this.field = null;
    }

    public String getField() {
        return field;
    }
}
