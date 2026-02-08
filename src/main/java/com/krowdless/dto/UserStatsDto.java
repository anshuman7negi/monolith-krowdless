package com.krowdless.dto;

import java.math.BigDecimal;

public class UserStatsDto {

    private Long userId;
    private String username;
    private String profileImageUrl;

    private Long followersCount;
    private Integer totalPoints;
    private Integer totalPlacesVisited;
    private BigDecimal totalSpent;

    private String titleName;
    private String titleIcon;

    
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getProfileImageUrl() {
        return profileImageUrl;
    }
    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
    public Long getFollowersCount() {
        return followersCount;
    }
    public void setFollowersCount(Long followersCount) {
        this.followersCount = followersCount;
    }
    public Integer getTotalPoints() {
        return totalPoints;
    }
    public void setTotalPoints(Integer totalPoints) {
        this.totalPoints = totalPoints;
    }
    public Integer getTotalPlacesVisited() {
        return totalPlacesVisited;
    }
    public void setTotalPlacesVisited(Integer totalPlacesVisited) {
        this.totalPlacesVisited = totalPlacesVisited;
    }
    public BigDecimal getTotalSpent() {
        return totalSpent;
    }
    public void setTotalSpent(BigDecimal totalSpent) {
        this.totalSpent = totalSpent;
    }
    public String getTitleName() {
        return titleName;
    }
    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }
    public String getTitleIcon() {
        return titleIcon;
    }
    public void setTitleIcon(String titleIcon) {
        this.titleIcon = titleIcon;
    }

    // getters & setters
    
}
