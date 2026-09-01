package com.rewards.exception;

import org.springframework.http.HttpStatus;

/**
 * Standard payload returned by the API when an error occurs.  
 */
public class ErrorResponse {
    private HttpStatus status;
    private String error;
    private String message;

    /**
     * Creates an error object with HTTP status and message details.
     *
     * @param status HTTP status code for the error response
     * @param error error category
     * @param message detailed error message
     */
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
