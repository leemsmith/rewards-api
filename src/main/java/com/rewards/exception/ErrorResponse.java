package com.rewards.exception;

import org.springframework.http.HttpStatus;

public class ErrorResponse {
    private HttpStatus status;
    private String error;
    private String message;

    public ErrorResponse(HttpStatus status, String error, String message) {
        this.status = status;
        this.error = error;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }
}
