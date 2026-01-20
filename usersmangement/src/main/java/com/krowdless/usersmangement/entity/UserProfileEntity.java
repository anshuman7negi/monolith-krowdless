package com.krowdless.usersmangement.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "user_profile")
public class UserProfileEntity {

    @Id
    private Long userId;

    @Column(length = 1000)
    private String about;

    @Column(length = 30)
    private String explorerLevel;

    @Column(length = 100)
    private String favoriteDestination;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getExplorerLevel() {
        return explorerLevel;
    }

    public void setExplorerLevel(String explorerLevel) {
        this.explorerLevel = explorerLevel;
    }

    public String getFavoriteDestination() {
        return favoriteDestination;
    }

    public void setFavoriteDestination(String favoriteDestination) {
        this.favoriteDestination = favoriteDestination;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // getters/setters
    
}
