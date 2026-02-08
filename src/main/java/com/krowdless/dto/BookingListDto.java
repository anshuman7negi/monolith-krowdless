package com.krowdless.dto;

import java.time.LocalDate;

public class BookingListDto {

    private Long bookingId;

    private Long stayId;
    private String stayTitle;
    private String stayImageUrl;
    private String stayLocation;

    private LocalDate checkInDate;
    private LocalDate checkOutDate;

    private int guests;
    private int nights;

    private double totalAmount;

    private String bookingStatus; // UPCOMING / COMPLETED / CANCELLED

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public Long getStayId() {
        return stayId;
    }

    public void setStayId(Long stayId) {
        this.stayId = stayId;
    }

    public String getStayTitle() {
        return stayTitle;
    }

    public void setStayTitle(String stayTitle) {
        this.stayTitle = stayTitle;
    }

    public String getStayImageUrl() {
        return stayImageUrl;
    }

    public void setStayImageUrl(String stayImageUrl) {
        this.stayImageUrl = stayImageUrl;
    }

    public String getStayLocation() {
        return stayLocation;
    }

    public void setStayLocation(String stayLocation) {
        this.stayLocation = stayLocation;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public int getGuests() {
        return guests;
    }

    public void setGuests(int guests) {
        this.guests = guests;
    }

    public int getNights() {
        return nights;
    }

    public void setNights(int nights) {
        this.nights = nights;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

}
