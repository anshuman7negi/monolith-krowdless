package com.krowdless.entity;

import java.math.BigDecimal;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "stay_draft_pricing")
public class StayDraftPricing {

    @Id
    @Column(name = "stay_draft_id")
    private Long stayDraftId;

    @Column(name = "price_per_night")
    private BigDecimal pricePerNight;

    private Integer minNights;
    private Integer maxNights;

    private LocalTime checkInTime;
    private LocalTime checkOutTime;

    public Long getStayDraftId() {
        return stayDraftId;
    }

    public void setStayDraftId(Long stayDraftId) {
        this.stayDraftId = stayDraftId;
    }

    public BigDecimal getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(BigDecimal pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public Integer getMinNights() {
        return minNights;
    }

    public void setMinNights(Integer minNights) {
        this.minNights = minNights;
    }

    public Integer getMaxNights() {
        return maxNights;
    }

    public void setMaxNights(Integer maxNights) {
        this.maxNights = maxNights;
    }

    public LocalTime getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(LocalTime checkInTime) {
        this.checkInTime = checkInTime;
    }

    public LocalTime getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(LocalTime checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

}
