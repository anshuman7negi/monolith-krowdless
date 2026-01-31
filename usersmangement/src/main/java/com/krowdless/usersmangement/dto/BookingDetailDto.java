package com.krowdless.usersmangement.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;

public class BookingDetailDto {

    // ===== BASIC =====
    private Long bookingId;
    private String bookingStatus;
    private OffsetDateTime bookedAt;

    // ===== STAY =====
    private Long stayId;
    private String stayTitle;
    private String stayImageUrl;
    private String stayAddress;
    private String propertyType;

    // ===== TRIP =====
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private int nights;
    private int guests;

    // ===== CHECK-IN INFO =====
    private LocalTime checkInTime;
    private LocalTime checkOutTime;

    // ===== PRICE =====
    private double pricePerNight;
    private double baseAmount;
    private double serviceFee;
    private double taxes;
    private double finalAmount;

    // ===== PAYMENT =====
    private String paymentStatus;
    private String paymentMethod;
    private String transactionId;

    // ===== HOST =====
    private Long hostId;
    private String hostName;
    private String hostProfileImage;
    private boolean hostVerified;

    // ===== CANCELLATION =====
    private boolean cancelled;
    private String cancelledBy;
    private double refundAmount;

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public OffsetDateTime getBookedAt() {
        return bookedAt;
    }

    public void setBookedAt(OffsetDateTime bookedAt) {
        this.bookedAt = bookedAt;
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

    public String getStayAddress() {
        return stayAddress;
    }

    public void setStayAddress(String stayAddress) {
        this.stayAddress = stayAddress;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
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

    public int getNights() {
        return nights;
    }

    public void setNights(int nights) {
        this.nights = nights;
    }

    public int getGuests() {
        return guests;
    }

    public void setGuests(int guests) {
        this.guests = guests;
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

    public double getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public double getBaseAmount() {
        return baseAmount;
    }

    public void setBaseAmount(double baseAmount) {
        this.baseAmount = baseAmount;
    }

    public double getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(double serviceFee) {
        this.serviceFee = serviceFee;
    }

    public double getTaxes() {
        return taxes;
    }

    public void setTaxes(double taxes) {
        this.taxes = taxes;
    }

    public double getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(double finalAmount) {
        this.finalAmount = finalAmount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Long getHostId() {
        return hostId;
    }

    public void setHostId(Long hostId) {
        this.hostId = hostId;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getHostProfileImage() {
        return hostProfileImage;
    }

    public void setHostProfileImage(String hostProfileImage) {
        this.hostProfileImage = hostProfileImage;
    }

    public boolean isHostVerified() {
        return hostVerified;
    }

    public void setHostVerified(boolean hostVerified) {
        this.hostVerified = hostVerified;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public String getCancelledBy() {
        return cancelledBy;
    }

    public void setCancelledBy(String cancelledBy) {
        this.cancelledBy = cancelledBy;
    }

    public double getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(double refundAmount) {
        this.refundAmount = refundAmount;
    }

}
