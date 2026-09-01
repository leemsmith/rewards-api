package com.rewards.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Centralizes exception handling across the REST API and converts domain errors into consistent HTTP responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Generates a 404 response for missing customers.
     *
     * @param exception exception containing the missing customer ID
     * @return HTTP 404 response with an error message
     */
    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCustomerNotFound(
            CustomerNotFoundException exception) {
        ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND, "Customer Not Found", exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}