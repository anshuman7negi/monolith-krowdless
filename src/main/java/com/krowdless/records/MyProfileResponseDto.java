package com.krowdless.records;

public record MyProfileResponseDto(
        Long id,
        String username,
        String email,
        String phone,
        String role,
        String status,
        boolean emailVerified,
        boolean phoneVerified,
        String profileImageUrl
) {}

