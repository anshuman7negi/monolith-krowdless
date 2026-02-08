package com.krowdless.service;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Service;

import com.krowdless.entity.OtpEntity;
import com.krowdless.entity.UserEntity;
import com.krowdless.enums.OtpType;
import com.krowdless.exception.BadRequestException;
import com.krowdless.repository.OtpRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OtpService {

    private static final int OTP_LENGTH = 6;
    private static final long OTP_EXPIRY_MINUTES = 10;

    private final OtpRepository otpRepository;
    private final EmailService emailService;
    private final SmsService smsService;

    // ================= GENERATE & SEND OTP =================
    @Transactional
    public void generateAndSendOtp(UserEntity user, OtpType type) {

        // 1. Remove old OTPs of same type (optional but cleaner)
        otpRepository.deleteByUserAndType(user, type);

        // 2. Generate OTP
        String code = generateOtpCode();

        // 3. Create OTP entity
        OtpEntity otp = OtpEntity.builder()
                .user(user)
                .code(code)
                .type(type)
                .expiresAt(Instant.now().plus(OTP_EXPIRY_MINUTES, ChronoUnit.MINUTES))
                .build();

        otpRepository.save(otp);

        // 4. Send OTP (based on type)
        sendOtp(user, code, type);
    }

    // ================= VERIFY OTP =================
    @Transactional
    public void verifyOtp(UserEntity user, String code, OtpType type) {

        OtpEntity otp = otpRepository
                .findTopByUserAndTypeAndUsedFalseOrderByCreatedAtDesc(user, type)
                .orElseThrow(() ->
                        new BadRequestException("OTP not found or already used"));

        // 1. Check expiry
        if (otp.getExpiresAt().isBefore(Instant.now())) {
            throw new BadRequestException("OTP has expired");
        }

        // 2. Check code
        if (!otp.getCode().equals(code)) {
            throw new BadRequestException("Invalid OTP");
        }

        // 3. Mark as used
        otp.setUsed(true);
        otpRepository.save(otp);
    }

    // ================= PRIVATE HELPERS =================
    private String generateOtpCode() {
        SecureRandom random = new SecureRandom();

        int bound = (int) Math.pow(10, OTP_LENGTH);
        int min = bound / 10;

        int otp = min + random.nextInt(bound - min);

        return String.valueOf(otp);
    }

    private void sendOtp(UserEntity user, String code, OtpType type) {

        switch (type) {
            case EMAIL_VERIFICATION -> {
                emailService.sendOtpEmail(
                        user.getEmail(),
                        code,
                        "Email Verification OTP"
                );
            }

            case PASSWORD_RESET -> {
                emailService.sendOtpEmail(
                        user.getEmail(),
                        code,
                        "Password Reset OTP"
                );
            }

            case PHONE_VERIFICATION -> {
                smsService.sendOtp(user.getPhone(), code);
            }
        }
    }
}
