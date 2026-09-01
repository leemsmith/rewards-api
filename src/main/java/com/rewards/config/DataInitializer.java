package com.rewards.config;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.rewards.model.Customer;
import com.rewards.model.Transaction;
import com.rewards.repository.CustomerRepository;
import com.rewards.repository.TransactionRepository;

/**
 * Loads a set of sample customer and transaction records when the application starts, used for
 * demonstrating the Rewards API.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final CustomerRepository customerRepository;
    private final TransactionRepository transactionRepository;

    /**
     * Creates the initializer with the required repositories.
     *
     * @param customerRepository repository used to persist customer records
     * @param transactionRepository repository used to persist transaction records
     */
    public DataInitializer(CustomerRepository customerRepository, TransactionRepository transactionRepository) {
        this.customerRepository = customerRepository;
        this.transactionRepository = transactionRepository;
    }

    /**
     * Seeds the database with sample customers and transactions.
     *
     * @param args runtime arguments passed by Spring Boot
     */
    @Override
    public void run(String... args) {
        Customer anna = customerRepository.save(new Customer("Anna", "Smith"));
        Customer bob = customerRepository.save(new Customer("Bob", "Williams"));
        Customer sue = customerRepository.save(new Customer("Sue", "Johnson"));

        List<Transaction> transactions = new ArrayList<>();

        transactions.add(new Transaction(anna, new BigDecimal("15.99"), date(Month.JUNE, 5)));
        transactions.add(new Transaction(anna, new BigDecimal("10.50"), date(Month.JUNE, 12)));
        transactions.add(new Transaction(anna, new BigDecimal("120.00"), date(Month.JULY, 3)));
        transactions.add(new Transaction(anna, new BigDecimal("49.97"), date(Month.AUGUST, 9)));

        transactions.add(new Transaction(bob, new BigDecimal("50.00"), date(Month.JUNE, 14)));
        transactions.add(new Transaction(bob, new BigDecimal("26.75"), date(Month.JULY, 7)));
        transactions.add(new Transaction(bob, new BigDecimal("63.25"), date(Month.JULY, 22)));
        transactions.add(new Transaction(bob, new BigDecimal("155.95"), date(Month.AUGUST, 16)));

        transactions.add(new Transaction(sue, new BigDecimal("97.80"), date(Month.JUNE, 9)));
        transactions.add(new Transaction(sue, new BigDecimal("9.77"), date(Month.JUNE, 20)));
        transactions.add(new Transaction(sue, new BigDecimal("100.00"), date(Month.JULY, 20)));
        transactions.add(new Transaction(sue, new BigDecimal("100.01"), date(Month.AUGUST, 29)));

        transactionRepository.saveAll(transactions);
    }

    /**
     * Creates a date for the seeded records using a fixed sample year.
     *
     * @param month target month for the transaction
     * @param dayOfMonth target day of month for the transaction
     * @return a LocalDate used in the seed dataset
     */
    private LocalDate date(Month month, int dayOfMonth) {
        return LocalDate.of(2026, month, dayOfMonth);
    }
}