package com.rewards.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.rewards.dto.RewardsDTO;
import com.rewards.exception.CustomerNotFoundException;
import com.rewards.model.Customer;
import com.rewards.model.Transaction;
import com.rewards.repository.CustomerRepository;
import com.rewards.repository.TransactionRepository;

/**
 * Unit tests for {@link RewardsService}.
 * Verifies customer validation and correct reward point calculation and aggregation.
 */
@ExtendWith(MockitoExtension.class)
class RewardsServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private RewardsService rewardsService;

    /**
     * Verifies that a CustomerNotFoundException is thrown when the customer does not exist in the database.
     */
    @Test
    void calculateRewards_throwsWhenCustomerDoesNotExist() {
        when(customerRepository.existsById(99L)).thenReturn(false);

        CustomerNotFoundException exception = assertThrows(CustomerNotFoundException.class,
                () -> rewardsService.calculateRewards(99L));

        assertEquals("Customer not found: 99", exception.getMessage());
        verify(transactionRepository, never()).findByCustomerId(99L);
    }

    /**
     * Verifies that monthly and total reward points are correctly calculated from a customer's transactions.
     */
    @Test
    void calculateRewards_returnsMonthlyAndTotalRewardsForCustomer() {
        Customer customer = new Customer("Jane", "Doe");
        customer.setId(7L);

        when(customerRepository.existsById(7L)).thenReturn(true);
        when(transactionRepository.findByCustomerId(7L)).thenReturn(List.of(
                new Transaction(customer, new BigDecimal("100.75"), LocalDate.of(2026, 6, 10)),
                new Transaction(customer, new BigDecimal("120.00"), LocalDate.of(2026, 6, 20)),
                new Transaction(customer, new BigDecimal("40.00"), LocalDate.of(2026, 7, 5))
        ));

        RewardsDTO result = rewardsService.calculateRewards(7L);

        assertEquals(7L, result.getCustomerId());
        assertEquals(Map.of(
                YearMonth.of(2026, 6), 140,
                YearMonth.of(2026, 7), 0
        ), result.getMonthlyRewards());
        assertEquals(140, result.getTotalRewards());
        verify(transactionRepository).findByCustomerId(7L);
    }

    /**
     * Verifies that reward calculation correctly handles boundary amounts:
     * $50 (boundary between no reward and 1 point per dollar)
     * $100 (boundary between 1 point and 2 points per dollar)
     */
    @Test
    void calculateRewards_handlesRewardCalculationBoundaries() {
        Customer customer = new Customer("John", "Smith");
        customer.setId(10L);

        when(customerRepository.existsById(10L)).thenReturn(true);
        when(transactionRepository.findByCustomerId(10L)).thenReturn(List.of(
                // At $50: should get 0 points
                new Transaction(customer, new BigDecimal("50.00"), LocalDate.of(2026, 6, 1)),
                
                // $50.01: should still get 0 points
                new Transaction(customer, new BigDecimal("50.01"), LocalDate.of(2026, 6, 2)),
                
                // At $100: should get 50 points (50 dollars * 1 point)
                new Transaction(customer, new BigDecimal("100.00"), LocalDate.of(2026, 6, 3)),
                
                // $100.01: should still get 50 points (50 dollars * 1 point)
                new Transaction(customer, new BigDecimal("100.01"), LocalDate.of(2026, 6, 4))
        ));

        RewardsDTO result = rewardsService.calculateRewards(10L);

        assertEquals(10L, result.getCustomerId());
        
        // 0 + 0 + 50 + 50 = 100
        assertEquals(100, result.getTotalRewards());
        assertEquals(Map.of(YearMonth.of(2026, 6), 100), result.getMonthlyRewards());
    }

    /**
     * Verifies that multiple transactions in a single month are correctly aggregated.
     */
    @Test
    void calculateRewards_aggregatesMultipleTransactionsInSingleMonth() {
        Customer customer = new Customer("Alice", "Johnson");
        customer.setId(11L);

        when(customerRepository.existsById(11L)).thenReturn(true);
        when(transactionRepository.findByCustomerId(11L)).thenReturn(List.of(
                new Transaction(customer, new BigDecimal("60.00"), LocalDate.of(2026, 7, 5)),
                new Transaction(customer, new BigDecimal("75.00"), LocalDate.of(2026, 7, 10)),
                new Transaction(customer, new BigDecimal("85.00"), LocalDate.of(2026, 7, 15)),
                new Transaction(customer, new BigDecimal("150.00"), LocalDate.of(2026, 7, 25))
        ));

        RewardsDTO result = rewardsService.calculateRewards(11L);

        assertEquals(11L, result.getCustomerId());
        // $60: 10 points
        // $75: 25 points
        // $85: 35 points
        // $150: 50 + 100 = 150 points
        // Total: 220 points
        assertEquals(220, result.getTotalRewards());
        assertEquals(Map.of(YearMonth.of(2026, 7), 220), result.getMonthlyRewards());
    }

    /**
     * Verifies that transactions across multiple months are correctly categorized and summed.
     */
    @Test
    void calculateRewards_aggregatesTransactionsAcrossMultipleMonths() {
        Customer customer = new Customer("Bob", "Williams");
        customer.setId(12L);

        when(customerRepository.existsById(12L)).thenReturn(true);
        when(transactionRepository.findByCustomerId(12L)).thenReturn(List.of(
                new Transaction(customer, new BigDecimal("120.00"), LocalDate.of(2026, 6, 10)),
                new Transaction(customer, new BigDecimal("80.00"), LocalDate.of(2026, 7, 15)),
                new Transaction(customer, new BigDecimal("110.00"), LocalDate.of(2026, 8, 20))
        ));

        RewardsDTO result = rewardsService.calculateRewards(12L);

        assertEquals(12L, result.getCustomerId());
        assertEquals(Map.of(
                YearMonth.of(2026, 6), 90,    // $120: 50 + (20 * 2) = 90 points
                YearMonth.of(2026, 7), 30,    // $80: 30 points
                YearMonth.of(2026, 8), 70     // $110: 50 + (10 * 2) = 70 points
        ), result.getMonthlyRewards());

        // Total: 90 + 30 + 70 = 190 points
        assertEquals(190, result.getTotalRewards());
    }

    /**
     * Verifies that a customer with no transactions returns zero rewards for all months and total.
     */
    @Test
    void calculateRewards_returnsZeroRewardsForCustomerWithNoTransactions() {
        Customer customer = new Customer("Emma", "Brown");
        customer.setId(13L);

        when(customerRepository.existsById(13L)).thenReturn(true);
        when(transactionRepository.findByCustomerId(13L)).thenReturn(List.of());

        RewardsDTO result = rewardsService.calculateRewards(13L);

        assertEquals(13L, result.getCustomerId());
        assertEquals(Map.of(), result.getMonthlyRewards());
        assertEquals(0, result.getTotalRewards());
        verify(transactionRepository).findByCustomerId(13L);
    }
}
