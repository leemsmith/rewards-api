package com.rewards.controller;

import com.rewards.dto.RewardsDTO;
import com.rewards.service.RewardsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller that exposes reward point totals for customers.
 */
@RestController
public class RewardsController {

    private final RewardsService rewardsService;

    public RewardsController(RewardsService rewardsService) {
        this.rewardsService = rewardsService;
    }

    /**
     * Retrieves the monthly and total reward points for a given customer.
     *
     * @param customerId ID of the customer
     * @return reward details for the customer
     */
    @GetMapping("/rewards/{customerId}")
    public RewardsDTO getRewards(@PathVariable("customerId") Long customerId) {
        return rewardsService.calculateRewards(customerId);
    }
}