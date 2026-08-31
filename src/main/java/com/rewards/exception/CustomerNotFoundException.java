package com.rewards.exception;

/**
 * CustomerNotFoundException
 */
public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(Long customerId) {
        super("Customer not found: " + customerId);
    }
}
