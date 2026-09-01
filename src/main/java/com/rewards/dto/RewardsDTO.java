package com.rewards.dto;

import java.time.YearMonth;
import java.util.Map;

/**
 * Response object returned by the Rewards API.  It includes the reward totals by month and the
 * overall total for a customer.
 */
public class RewardsDTO {

    private Long customerId;

    private Map<YearMonth, Integer> monthlyRewards;
    
    private Integer totalRewards;

    public RewardsDTO() {
    }

    /**
     * Creates a reward summary payload for a customer.
     *
     * @param customerId ID of the customer
     * @param monthlyRewards reward points grouped by month
     * @param totalRewards cumulative points across all months
     */
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
