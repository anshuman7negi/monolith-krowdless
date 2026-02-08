package com.krowdless.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalTime;

@Entity
@Table(name = "stay_pricing")
public class StayPricing {

    @Id
    @Column(name = "stay_id")
    private Long stayId;

    @Column(name = "price_per_night", nullable = false)
    private BigDecimal pricePerNight;

    @Column(name = "min_nights")
    private Integer minNights;

    @Column(name = "max_nights")
    private Integer maxNights;

    @Column(name = "check_in_time")
    private LocalTime checkInTime;

    @Column(name = "check_out_time")
    private LocalTime checkOutTime;

    /* ================= GETTERS / SETTERS ================= */

    public Long getStayId() { return stayId; }
    public void setStayId(Long stayId) { this.stayId = stayId; }

    public BigDecimal getPricePerNight() { return pricePerNight; }
    public void setPricePerNight(BigDecimal pricePerNight) { this.pricePerNight = pricePerNight; }

    public Integer getMinNights() { return minNights; }
    public void setMinNights(Integer minNights) { this.minNights = minNights; }

    public Integer getMaxNights() { return maxNights; }
    public void setMaxNights(Integer maxNights) { this.maxNights = maxNights; }

    public LocalTime getCheckInTime() { return checkInTime; }
    public void setCheckInTime(LocalTime checkInTime) { this.checkInTime = checkInTime; }

    public LocalTime getCheckOutTime() { return checkOutTime; }
    public void setCheckOutTime(LocalTime checkOutTime) { this.checkOutTime = checkOutTime; }
}
