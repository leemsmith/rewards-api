package com.rewards.dto;

import java.time.YearMonth;
import java.util.Map;

public class RewardsDTO {

    private Long customerId;

    private Map<YearMonth, Integer> monthlyRewards;
    
    private Integer totalRewards;

    public RewardsDTO() {
}

    public RewardsDTO(Long customerId, Map<YearMonth, Integer> monthlyRewards, Integer totalRewards) {
        this.customerId = customerId;
        this.monthlyRewards = monthlyRewards;
        this.totalRewards = totalRewards;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Map<YearMonth, Integer> getMonthlyRewards() {
        return monthlyRewards;
    }

    public void setMonthlyRewards(Map<YearMonth, Integer> monthlyRewards) {
        this.monthlyRewards = monthlyRewards;
    }

    public Integer getTotalRewards() {
        return totalRewards;
    }

    public void setTotalRewards(Integer totalRewards) {
        this.totalRewards = totalRewards;
    }
}
