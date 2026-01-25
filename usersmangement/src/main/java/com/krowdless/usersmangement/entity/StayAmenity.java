package com.krowdless.usersmangement.entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "stay_amenity")
@IdClass(StayAmenityId.class)
public class StayAmenity {

    @Id
    @Column(name = "stay_id")
    private Long stayId;

    @Id
    @Column(name = "amenity_code")
    private String amenityCode;

    /* ================= GETTERS / SETTERS ================= */

    public Long getStayId() { return stayId; }
    public void setStayId(Long stayId) { this.stayId = stayId; }

    public String getAmenityCode() { return amenityCode; }
    public void setAmenityCode(String amenityCode) { this.amenityCode = amenityCode; }
}
