package com.rewards.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RewardsController {

    @GetMapping("/rewards")
    public String getRewards() {
        return "Rewards points";
    }
}