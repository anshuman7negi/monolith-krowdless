package com.krowdless.usersmangement.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "destination_crowd")
public class DestinationCrowd {

    @Id
    @Column(name = "destination_id")
    private Long destinationId;

    @Column(name = "crowd_level")
    private String crowdLevel;

    @Column(name = "crowd_score")
    private BigDecimal crowdScore;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Long getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(Long destinationId) {
        this.destinationId = destinationId;
    }

    public String getCrowdLevel() {
        return crowdLevel;
    }

    public void setCrowdLevel(String crowdLevel) {
        this.crowdLevel = crowdLevel;
    }

    public BigDecimal getCrowdScore() {
        return crowdScore;
    }

    public void setCrowdScore(BigDecimal crowdScore) {
        this.crowdScore = crowdScore;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    

}
