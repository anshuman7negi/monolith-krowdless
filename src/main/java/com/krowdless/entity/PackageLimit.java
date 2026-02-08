package com.krowdless.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "package_limits", uniqueConstraints = @UniqueConstraint(columnNames = "user_id"))
public class PackageLimit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "total_allowed")
    private Integer totalAllowed = 3;

    @Column(name = "total_created")
    private Integer totalCreated = 0;

    @Column(name = "is_subscription_active")
    private Boolean isSubscriptionActive = false;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getTotalAllowed() {
        return totalAllowed;
    }

    public void setTotalAllowed(Integer totalAllowed) {
        this.totalAllowed = totalAllowed;
    }

    public Integer getTotalCreated() {
        return totalCreated;
    }

    public void setTotalCreated(Integer totalCreated) {
        this.totalCreated = totalCreated;
    }

    public Boolean getIsSubscriptionActive() {
        return isSubscriptionActive;
    }

    public void setIsSubscriptionActive(Boolean isSubscriptionActive) {
        this.isSubscriptionActive = isSubscriptionActive;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

}
