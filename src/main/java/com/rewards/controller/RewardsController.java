package com.rewards.controller;

import com.rewards.service.RewardsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RewardsController {

    private final RewardsService rewardsService;

    public RewardsController(RewardsService rewardsService) {
        this.rewardsService = rewardsService;
    }

    @GetMapping("/rewards")
    public String getRewards() {
        Integer rewards = rewardsService.calculateRewards();
        return "Rewards points: " + rewards;
    }
}