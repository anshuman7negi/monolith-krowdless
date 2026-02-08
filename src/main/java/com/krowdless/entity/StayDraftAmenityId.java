package com.krowdless.entity;

import java.io.Serializable;

public class StayDraftAmenityId implements Serializable {
    private Long stayDraftId;
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
