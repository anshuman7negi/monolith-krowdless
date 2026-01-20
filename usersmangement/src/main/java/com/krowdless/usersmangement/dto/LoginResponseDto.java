package com.krowdless.usersmangement.dto;

public class LoginResponseDto {
    private Long userId;
    private String token;
    private String refreshToken;
    private String username;
    private String role;
    private boolean verified;

    public LoginResponseDto(Long userId, String token, String refreshToken, String username, String role, boolean verified) {
        this.userId = userId;
        this.token = token;
        this.refreshToken = refreshToken;
        this.username = username;
        this.role = role;
        this.verified = verified;
    }

    public Long getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

}
