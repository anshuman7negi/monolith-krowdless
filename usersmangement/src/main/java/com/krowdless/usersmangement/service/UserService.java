package com.krowdless.usersmangement.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.krowdless.usersmangement.config.JwtUtil;
import com.krowdless.usersmangement.dto.LoginResponseDto;
import com.krowdless.usersmangement.dto.UserRegisterRequestDto;
import com.krowdless.usersmangement.dto.UserResponseDto;
import com.krowdless.usersmangement.entity.BlacklistedTokenEntity;
import com.krowdless.usersmangement.entity.RefreshTokenEntity;
import com.krowdless.usersmangement.entity.UserEntity;
import com.krowdless.usersmangement.entity.UserRole;
import com.krowdless.usersmangement.repository.BlacklistedTokenRepository;
import com.krowdless.usersmangement.repository.RefreshTokenRepository;
import com.krowdless.usersmangement.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private BlacklistedTokenRepository blacklistedTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private SupabaseStorageService supabaseStorageService;

    public UserEntity findByUsername(String username) {
        return repository.findByUsername(username).orElse(null);
    }

    @Transactional
    public UserResponseDto register(UserRegisterRequestDto dto) {
        UserEntity user = new UserEntity();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setRole(dto.getRole());

        if (dto.getRole() == UserRole.PARTNER) {
            user.setVerified(false); // needs admin approval
        } else {
            user.setVerified(true); // normal users are auto-verified
        }

        return toResponseDto(repository.save(user));
    }

    public LoginResponseDto login(String email, String password) {
        return repository.findByEmail(email)
                .filter(u -> passwordEncoder.matches(password, u.getPassword()))
                .map(u -> {
                    String jti = java.util.UUID.randomUUID().toString();

                    // Access token
                    String accessToken = jwtUtil.generateAccessToken(
                            u.getUsername(),
                            u.getRole().name(),
                            u.isVerified(),
                            u.getId(),
                            jti);

                    // Refresh token (entity based)
                    RefreshTokenEntity rt = new RefreshTokenEntity();
                    rt.setToken(java.util.UUID.randomUUID().toString());
                    rt.setUserId(u.getId());
                    rt.setExpiresAt(LocalDateTime.now().plusDays(15));
                    rt.setRevoked(false);
                    refreshTokenRepository.save(rt);

                    return new LoginResponseDto(
                            accessToken,
                            rt.getToken(),
                            u.getUsername(),
                            u.getRole().name(),
                            u.isVerified());
                })
                .orElse(null);
    }

    private UserResponseDto toResponseDto(UserEntity entity) {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(entity.getId());
        dto.setUsername(entity.getUsername());
        dto.setEmail(entity.getEmail());
        dto.setPhone(entity.getPhone());
        dto.setRole(entity.getRole().name());
        dto.setVerified(entity.isVerified());
        return dto;
    }

    public void softDeleteUser(Long id) {
        UserEntity u = repository.findById(id).orElseThrow();
        u.setDeleted(true);
        u.setActive(false);
        u.setDeletedAt(LocalDateTime.now());
        repository.save(u);
    }

    public LoginResponseDto refresh(String refreshToken) {
        var rt = refreshTokenRepository.findByTokenAndRevokedFalse(refreshToken).orElse(null);
        if (rt == null || rt.getExpiresAt().isBefore(LocalDateTime.now())) {
            return null;
        }
        UserEntity u = repository.findById(rt.getUserId()).orElseThrow();

        String jti = java.util.UUID.randomUUID().toString();
        String newAccess = jwtUtil.generateAccessToken(u.getUsername(), u.getRole().name(), u.isVerified(), u.getId(),
                jti);

        // Rotate refresh token
        rt.setRevoked(true);
        refreshTokenRepository.save(rt);

        RefreshTokenEntity newRt = new RefreshTokenEntity();
        newRt.setToken(java.util.UUID.randomUUID().toString());
        newRt.setUserId(u.getId());
        newRt.setExpiresAt(LocalDateTime.now().plusDays(15));
        refreshTokenRepository.save(newRt);

        return new LoginResponseDto(newAccess, newRt.getToken(), u.getUsername(), u.getRole().name(), u.isVerified());
    }

    public void logout(String accessToken, String refreshToken) {
        if (accessToken != null && accessToken.startsWith("Bearer ")) {
            String token = accessToken.substring(7);
            String jti = jwtUtil.extractJti(token);
            LocalDateTime exp = jwtUtil.extractExpiration(token).toInstant()
                    .atZone(java.time.ZoneId.systemDefault()).toLocalDateTime();

            BlacklistedTokenEntity b = new BlacklistedTokenEntity();
            b.setJti(jti);
            b.setExpiresAt(exp);
            blacklistedTokenRepository.save(b);
        }

        if (refreshToken != null) {
            refreshTokenRepository.findByTokenAndRevokedFalse(refreshToken)
                    .ifPresent(rt -> {
                        rt.setRevoked(true);
                        refreshTokenRepository.save(rt);
                    });
        }
    }

    @Transactional
    public UserResponseDto uploadProfilePhoto(Long userId, MultipartFile file) {

        UserEntity user = repository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 1️⃣ Delete old image from bucket (if exists)
        if (user.getProfileImageUrl() != null) {
            supabaseStorageService.deleteUserProfile(user.getProfileImageUrl());
        }

        // 2️⃣ Upload new image
        String imageUrl = supabaseStorageService.uploadUserProfile(userId, file);

        // 3️⃣ Update DB
        user.setProfileImageUrl(imageUrl);
        repository.save(user);

        return toResponseDto(user);
    }

    public String getProfilePhotoUrl(Long userId) {

        UserEntity user = repository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getProfileImageUrl() == null) {
            throw new RuntimeException("Profile photo not uploaded");
        }

        return user.getProfileImageUrl();
    }

}
