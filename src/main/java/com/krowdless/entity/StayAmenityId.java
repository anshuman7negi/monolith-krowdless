package com.krowdless.entity;

import java.io.Serializable;
import java.util.Objects;

public class StayAmenityId implements Serializable {

    private Long stayId;
    private String amenityCode;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StayAmenityId)) return false;
        StayAmenityId that = (StayAmenityId) o;
        return Objects.equals(stayId, that.stayId)
                && Objects.equals(amenityCode, that.amenityCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stayId, amenityCode);
    }
}
