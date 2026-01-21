package com.krowdless.usersmangement.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final Key secretKey;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
    }

    // Access token: 15 min
    private static final long ACCESS_EXP_MS = 15 * 60 * 1000;

    // Refresh token: 7 days
    private static final long REFRESH_EXP_MS = 7 * 24 * 60 * 60 * 1000;

    // Legacy token: 10 hours (can be removed if not used)
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 10;

    public String generateAccessToken(String username, String role, boolean verified, Long userId, String jti) {
        return Jwts.builder()
                .setSubject(username)
                .setId(jti) // jti
                .claim("role", role)
                .claim("verified", verified)
                .claim("userId", userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_EXP_MS))
                .signWith(secretKey)
                .compact();
    }

    public String generateRefreshToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_EXP_MS))
                .signWith(secretKey)
                .compact();
    }

    // For backward compatibility (10-hour token)
    public String generateToken(String username, String role, boolean verified, Long userId) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .claim("verified", verified)
                .claim("userId", userId)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(secretKey)
                .compact();
    }

    public String extractUsername(String token) {
        return getAllClaims(token).getSubject();
    }

    public String extractRole(String token) {
        return (String) getAllClaims(token).get("role");
    }

    public boolean extractVerified(String token) {
        return (Boolean) getAllClaims(token).get("verified");
    }

    public Long extractUserId(String token) {
        return ((Number) getAllClaims(token).get("userId")).longValue();
    }

    public String extractJti(String token) {
        return getAllClaims(token).getId();
    }

    public Date extractExpiration(String token) {
        return getAllClaims(token).getExpiration();
    }

    public boolean validateToken(String token) {
        return !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Claims getAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
