package com.krowdless.config;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtService {

    private final Key secretKey;

    // 15 minutes access token
    private static final long ACCESS_EXP_MS = 15 * 60 * 1000;

    // 7 days refresh token
    private static final long REFRESH_EXP_MS = 7 * 24 * 60 * 60 * 1000;

    public JwtService(@Value("${jwt.secret}") String secret) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }


    // ================= ACCESS TOKEN =================
    public String generateAccessToken(UserDetails userDetails, Long userId) {
        Map<String, Object> claims = new HashMap<>();

        claims.put("role",
                userDetails.getAuthorities().stream()
                        .findFirst()
                        .map(GrantedAuthority::getAuthority)
                        .orElse("ROLE_USER"));

        claims.put("userId", userId);

        return buildToken(claims, userDetails.getUsername(), ACCESS_EXP_MS);
    }

    // ================= REFRESH TOKEN =================
    public String generateRefreshToken(String username, String jti) {
        return Jwts.builder()
                .setSubject(username)
                .setId(jti)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_EXP_MS))
                .signWith(secretKey)
                .compact();
    }

    private String buildToken(Map<String, Object> claims, String subject, long expiration) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(secretKey)
                .compact();
    }

    // ================= CLAIM EXTRACTION =================
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public String extractRole(String token) {
        return (String) extractAllClaims(token).get("role");
    }

    public Long extractUserId(String token) {
        return ((Number) extractAllClaims(token).get("userId")).longValue();
    }

    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }
    
    public String extractJti(String token) {
        return extractAllClaims(token).getId();
    }

    // ================= VALIDATION =================
    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            return username.equals(userDetails.getUsername())
                    && !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
