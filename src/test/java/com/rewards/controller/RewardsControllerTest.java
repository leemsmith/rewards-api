package com.rewards.controller;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.YearMonth;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.rewards.dto.RewardsDTO;
import com.rewards.exception.CustomerNotFoundException;
import com.rewards.service.RewardsService;

/**
 * Unit tests for {@link RewardsController}.
 * Verifies successful responses and error handling for missing customers.
 */
@ExtendWith(MockitoExtension.class)
class RewardsControllerTest {

    @Mock
    private RewardsService rewardsService;

    @InjectMocks
    private RewardsController rewardsController;

    /**
     * Verifies that a successful request returns the reward summary from the service.
     */
    @Test
    void getRewards_returnsRewardSummaryForExistingCustomer() {
        RewardsDTO rewards = new RewardsDTO(42L, Map.of(YearMonth.of(2026, 7), 120), 120);
        when(rewardsService.calculateRewards(42L)).thenReturn(rewards);

        RewardsDTO result = rewardsController.getRewards(42L);

        assertSame(rewards, result);
        verify(rewardsService).calculateRewards(42L);
    }

    /**
     * Verifies that a request for a nonexistent customer throws CustomerNotFoundException.
     */
    @Test
    void getRewards_throwsForMissingCustomer() {
        when(rewardsService.calculateRewards(99L))
                .thenThrow(new CustomerNotFoundException(99L));

        assertThrows(CustomerNotFoundException.class,
                () -> rewardsController.getRewards(99L));
        verify(rewardsService).calculateRewards(99L);
    }
}
