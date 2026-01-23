package com.krowdless.usersmangement.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class DestinationCategoryId implements Serializable {

    @Column(name = "destination_id")
    private Long destinationId;

    @Column(name = "category_id")
    private Long categoryId;

    public DestinationCategoryId() {
    }

    public DestinationCategoryId(Long destinationId, Long categoryId) {
        this.destinationId = destinationId;
        this.categoryId = categoryId;
    }

    public Long getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(Long destinationId) {
        this.destinationId = destinationId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}
