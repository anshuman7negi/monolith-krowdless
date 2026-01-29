package com.krowdless.usersmangement.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "stay_booking_price")
public class StayBookingPrice {

    @Id
    @Column(name = "booking_id")
    private Long bookingId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "booking_id")
    private StayBooking booking;

    @Column(name = "price_per_night", nullable = false)
    private Double pricePerNight;

    @Column(nullable = false)
    private Integer nights;

    @Column(name = "base_amount", nullable = false)
    private Double baseAmount;

    @Column(name = "service_fee")
    private Double serviceFee;

    @Column
    private Double taxes;

    @Column(name = "final_amount", nullable = false)
    private Double finalAmount;

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public StayBooking getBooking() {
        return booking;
    }

    public void setBooking(StayBooking booking) {
        this.booking = booking;
    }

    public Double getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(Double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public Integer getNights() {
        return nights;
    }

    public void setNights(Integer nights) {
        this.nights = nights;
    }

    public Double getBaseAmount() {
        return baseAmount;
    }

    public void setBaseAmount(Double baseAmount) {
        this.baseAmount = baseAmount;
    }

    public Double getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(Double serviceFee) {
        this.serviceFee = serviceFee;
    }

    public Double getTaxes() {
        return taxes;
    }

    public void setTaxes(Double taxes) {
        this.taxes = taxes;
    }

    public Double getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(Double finalAmount) {
        this.finalAmount = finalAmount;
    }

}
