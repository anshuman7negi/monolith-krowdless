package com.krowdless.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "stay_booking_cancellation")
public class StayBookingCancellation {

    @Id
    @Column(name = "booking_id")
    private Long bookingId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "booking_id")
    private StayBooking booking;

    @Column(name = "cancelled_by", nullable = false)
    private String cancelledBy;

    private String reason;

    @Column(name = "refund_amount")
    private Double refundAmount;

    @Column(name = "cancelled_at")
    private OffsetDateTime cancelledAt;

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

    public String getCancelledBy() {
        return cancelledBy;
    }

    public void setCancelledBy(String cancelledBy) {
        this.cancelledBy = cancelledBy;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Double getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(Double refundAmount) {
        this.refundAmount = refundAmount;
    }

    public OffsetDateTime getCancelledAt() {
        return cancelledAt;
    }

    public void setCancelledAt(OffsetDateTime cancelledAt) {
        this.cancelledAt = cancelledAt;
    }

}
