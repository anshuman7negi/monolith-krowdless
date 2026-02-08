package com.krowdless.records;

public record LoginResponseDto(
        String accessToken,
        String refreshToken,
        String role,
        Long userId
) {}
