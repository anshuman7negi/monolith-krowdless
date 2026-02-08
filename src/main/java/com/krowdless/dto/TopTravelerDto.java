package com.krowdless.dto;

import java.math.BigDecimal;

public class TopTravelerDto {

    private Long userId;
    private String username;
    private String profileImageUrl;

    private Integer totalPoints;
    private Integer totalTrips;
    private BigDecimal totalSpent;

    private String titleName;
    private String titleIcon;

    private Long followerCount;

    public TopTravelerDto(
            Long userId,
            String username,
            String profileImageUrl,
            Integer totalPoints,
            Integer totalTrips,
            BigDecimal totalSpent,
            String titleName,
            String titleIcon,
            Long followerCount
    ) {
        this.userId = userId;
        this.username = username;
        this.profileImageUrl = profileImageUrl;
        this.totalPoints = totalPoints;
        this.totalTrips = totalTrips;
        this.totalSpent = totalSpent;
        this.titleName = titleName;
        this.titleIcon = titleIcon;
        this.followerCount = followerCount;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public Integer getTotalPoints() {
        return totalPoints;
    }

    public Integer getTotalTrips() {
        return totalTrips;
    }

    public BigDecimal getTotalSpent() {
        return totalSpent;
    }

    public String getTitleName() {
        return titleName;
    }

    public String getTitleIcon() {
        return titleIcon;
    }

    public Long getFollowerCount() {
        return followerCount;
    }

    // ✅ getters only (setter nahi chahiye)
    
}
