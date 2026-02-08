package com.krowdless.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;


@Entity
@Table(name = "destination_category")
public class DestinationCategory {

    @EmbeddedId
    private DestinationCategoryId id;

    public DestinationCategory() {}

    public DestinationCategory(Long destinationId, Long categoryId) {
        this.id = new DestinationCategoryId(destinationId, categoryId);
    }

    public DestinationCategoryId getId() {
        return id;
    }

    public void setId(DestinationCategoryId id) {
        this.id = id;
    }

    // convenience getters (OPTIONAL but useful)

    public Long getDestinationId() {
        return id != null ? id.getDestinationId() : null;
    }

    public Long getCategoryId() {
        return id != null ? id.getCategoryId() : null;
    }
}
