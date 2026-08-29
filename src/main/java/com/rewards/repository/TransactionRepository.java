package com.rewards.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rewards.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByCustomerId(Long customerId);
}
