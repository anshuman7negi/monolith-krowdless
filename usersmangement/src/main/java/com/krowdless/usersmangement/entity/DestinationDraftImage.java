package com.krowdless.usersmangement.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "destination_draft_image")
public class DestinationDraftImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "destination_draft_id", nullable = false)
    private Long destinationDraftId;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;

    @Column(name = "is_cover")
    private Boolean isCover = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    // ===== getters & setters =====

    public Long getId() {
        return id;
    }

    public Long getDestinationDraftId() {
        return destinationDraftId;
    }

    public void setDestinationDraftId(Long destinationDraftId) {
        this.destinationDraftId = destinationDraftId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Boolean getIsCover() {
        return isCover;
    }

    public void setIsCover(Boolean cover) {
        isCover = cover;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
