package com.krowdless.usersmangement.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "stay_capacity")
public class StayCapacity {

    @Id
    @Column(name = "stay_id")
    private Long stayId;

    @Column(name = "max_guests", nullable = false)
    private Integer maxGuests;

    private Integer bedrooms;
    private Integer beds;
    private Integer bathrooms;

    /* ================= GETTERS / SETTERS ================= */

    public Long getStayId() { return stayId; }
    public void setStayId(Long stayId) { this.stayId = stayId; }

    public Integer getMaxGuests() { return maxGuests; }
    public void setMaxGuests(Integer maxGuests) { this.maxGuests = maxGuests; }

    public Integer getBedrooms() { return bedrooms; }
    public void setBedrooms(Integer bedrooms) { this.bedrooms = bedrooms; }

    public Integer getBeds() { return beds; }
    public void setBeds(Integer beds) { this.beds = beds; }

    public Integer getBathrooms() { return bathrooms; }
    public void setBathrooms(Integer bathrooms) { this.bathrooms = bathrooms; }
}
