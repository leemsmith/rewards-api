package com.rewards.controller;

import com.rewards.service.RewardsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RewardsController {

    private final RewardsService rewardsService;

    public RewardsController(RewardsService rewardsService) {
        this.rewardsService = rewardsService;
    }

    @GetMapping("/rewards/{customerId}")
    public String getRewards(@PathVariable("customerId") Long customerId) {
        Integer rewards = rewardsService.calculateRewards(customerId);
        return "Rewards points: " + rewards;
    }
}