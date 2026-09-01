package com.rewards.service;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

import com.rewards.dto.RewardsDTO;
import com.rewards.exception.CustomerNotFoundException;
import com.rewards.model.Transaction;
import com.rewards.repository.CustomerRepository;
import com.rewards.repository.TransactionRepository;

@Service
public class RewardsService {

    private final CustomerRepository customerRepository;
    private final TransactionRepository transactionRepository;

    public RewardsService(CustomerRepository customerRepository, TransactionRepository transactionRepository) {
        this.customerRepository = customerRepository;
        this.transactionRepository = transactionRepository;
    }

    public RewardsDTO calculateRewards(Long customerId) {
        Map<YearMonth, Integer> monthlyTotals = new HashMap<>();

        // Check if customer exists
        if (!customerRepository.existsById(customerId)) {
            throw new CustomerNotFoundException(customerId);
        }

        // Calculate rewards for each transaction
        List<Transaction> transactions = transactionRepository.findByCustomerId(customerId);
        for (Transaction transaction : transactions) {
            YearMonth yearMonth = YearMonth.from(transaction.getTransactionDate());
            Integer rewardPoints = calculateRewardPoints(transaction.getAmount());
            monthlyTotals.merge(yearMonth, rewardPoints, Integer::sum);
        }
        
        // Calculate total rewards
        int totalRewards = monthlyTotals.values().stream().mapToInt(Integer::intValue).sum();

        // Return response object
        return new RewardsDTO(customerId, monthlyTotals, totalRewards);
    }

    private int calculateRewardPoints(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.valueOf(50)) <= 0) {
            return 0;
        } else if (amount.compareTo(BigDecimal.valueOf(100)) <= 0) {
            return amount.subtract(BigDecimal.valueOf(50)).intValue();
        } else {
            return 50 + (amount.subtract(BigDecimal.valueOf(100)).intValue() * 2);
        }
    }
}
