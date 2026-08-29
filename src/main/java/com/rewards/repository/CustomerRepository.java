package com.rewards.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rewards.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
