package com.krowdless.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@Table(name = "stay_draft_amenity")
@IdClass(StayDraftAmenityId.class)
public class StayDraftAmenity {

    @Id
    @Column(name = "stay_draft_id")
    private Long stayDraftId;

    @Id
    @Column(name = "amenity_code")
    private String amenityCode;

    public Long getStayDraftId() {
        return stayDraftId;
    }

    public void setStayDraftId(Long stayDraftId) {
        this.stayDraftId = stayDraftId;
    }

    public String getAmenityCode() {
        return amenityCode;
    }

    public void setAmenityCode(String amenityCode) {
        this.amenityCode = amenityCode;
    }

}
