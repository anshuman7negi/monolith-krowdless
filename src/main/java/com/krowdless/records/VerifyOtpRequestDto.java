package com.krowdless.records;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record VerifyOtpRequestDto(
        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "OTP is required")
        String otp
) {}
