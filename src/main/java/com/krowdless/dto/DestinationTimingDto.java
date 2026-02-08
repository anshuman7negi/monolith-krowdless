package com.krowdless.dto;

public class DestinationTimingDto {

    private int dayOfWeek;   // 1=Mon ... 7=Sun
    private String openTime; // "09:00"
    private String closeTime;
    private boolean closed;
    
    public int getDayOfWeek() {
        return dayOfWeek;
    }
    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
    public String getOpenTime() {
        return openTime;
    }
    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }
    public String getCloseTime() {
        return closeTime;
    }
    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }
    public boolean isClosed() {
        return closed;
    }
    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    // getters & setters

    
}

