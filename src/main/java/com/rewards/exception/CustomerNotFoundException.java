package com.rewards.exception;

/**
 * Thrown when a customer lookup does not match any record in the system.
 */
public class CustomerNotFoundException extends RuntimeException {
    /**
     * Creates an exception for the specified customer ID when no matching record is found.
     *
     * @param customerId ID of the customer not found
     */
    public CustomerNotFoundException(Long customerId) {
        super("Customer not found: " + customerId);
    }
}
