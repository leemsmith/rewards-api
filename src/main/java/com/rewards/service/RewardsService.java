package com.rewards.service;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

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

    public Integer calculateRewards(Long customerId) {
        Integer totalRewards = 0;
        if (!customerRepository.existsById(customerId)) {
            throw new CustomerNotFoundException(customerId);
        }
        Map<YearMonth, BigDecimal> monthlyTotals = getMonthlyTotals(customerId);
        for (Map.Entry<YearMonth, BigDecimal> entry : monthlyTotals.entrySet()) {
            BigDecimal total = entry.getValue();
            total = total.subtract(BigDecimal.valueOf(50));
            if (total.compareTo(BigDecimal.ZERO) > 0) {
                totalRewards += total.intValue();
            } else {
                break;
            }
            total = total.subtract(BigDecimal.valueOf(50));
            if (total.compareTo(BigDecimal.ZERO) > 0) {
                totalRewards += total.intValue();
            } else {
                break;
            }
        }
        return totalRewards;
    }

    private Map<YearMonth, BigDecimal> getMonthlyTotals(Long customerId) {
        List<Transaction> transactions = transactionRepository.findByCustomerId(customerId);
        return transactions.stream()
                .collect(Collectors.groupingBy(
                        t -> YearMonth.from(t.getTransactionDate()),
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                Transaction::getAmount,
                                BigDecimal::add
                        )
                ));
    }
}
