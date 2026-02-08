package com.krowdless.entity;


import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "stay_availability")
@IdClass(StayAvailabilityId.class)
public class StayAvailability {

    @Id
    @Column(name = "stay_id")
    private Long stayId;

    @Id
    @Column(name = "booking_date")
    private LocalDate bookingDate;

    @Column(nullable = false)
    private String status;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    private StayBooking booking;

    public Long getStayId() {
        return stayId;
    }

    public void setStayId(Long stayId) {
        this.stayId = stayId;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public StayBooking getBooking() {
        return booking;
    }

    public void setBooking(StayBooking booking) {
        this.booking = booking;
    }

    
}

