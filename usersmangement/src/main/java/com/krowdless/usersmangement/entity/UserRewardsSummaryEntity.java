package com.krowdless.usersmangement.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "user_rewards_summary")
public class UserRewardsSummaryEntity {

    @Id
    private Long userId;

    private Integer totalPoints;
    private Integer totalTrips;
    private BigDecimal totalSpent;
    
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public Integer getTotalPoints() {
        return totalPoints;
    }
    public void setTotalPoints(Integer totalPoints) {
        this.totalPoints = totalPoints;
    }
    public Integer getTotalTrips() {
        return totalTrips;
    }
    public void setTotalTrips(Integer totalTrips) {
        this.totalTrips = totalTrips;
    }
    public BigDecimal getTotalSpent() {
        return totalSpent;
    }
    public void setTotalSpent(BigDecimal totalSpent) {
        this.totalSpent = totalSpent;
    }

    // getters setters
    
}
