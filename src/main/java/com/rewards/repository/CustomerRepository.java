package com.rewards.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rewards.model.Customer;

/**
 * Data access layer for customer records.
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
