package com.krowdless.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "stay_draft_capacity")
public class StayDraftCapacity {

    @Id
    @Column(name = "stay_draft_id")
    private Long stayDraftId;

    private Integer maxGuests;
    private Integer bedrooms;
    private Integer beds;
    private Integer bathrooms;

    public Long getStayDraftId() {
        return stayDraftId;
    }

    public void setStayDraftId(Long stayDraftId) {
        this.stayDraftId = stayDraftId;
    }

    public Integer getMaxGuests() {
        return maxGuests;
    }

    public void setMaxGuests(Integer maxGuests) {
        this.maxGuests = maxGuests;
    }

    public Integer getBedrooms() {
        return bedrooms;
    }

    public void setBedrooms(Integer bedrooms) {
        this.bedrooms = bedrooms;
    }

    public Integer getBeds() {
        return beds;
    }

    public void setBeds(Integer beds) {
        this.beds = beds;
    }

    public Integer getBathrooms() {
        return bathrooms;
    }

    public void setBathrooms(Integer bathrooms) {
        this.bathrooms = bathrooms;
    }

}
