package com.krowdless.records;

import jakarta.validation.constraints.NotBlank;

public record VerifyResetOtpRequestDto(
        @NotBlank(message = "Identifier is required")
        String identifier,
        @NotBlank(message = "OTP is required")
        String otp
) {}
