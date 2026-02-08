package com.krowdless.records;

import jakarta.validation.constraints.NotBlank;

public record ForgotPasswordRequestDto(
        @NotBlank(message = "Identifier is required")
        String identifier
) {}
