package com.rewards.controller;

import com.rewards.dto.RewardsDTO;
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
    public RewardsDTO getRewards(@PathVariable("customerId") Long customerId) {
        return rewardsService.calculateRewards(customerId);
    }
}