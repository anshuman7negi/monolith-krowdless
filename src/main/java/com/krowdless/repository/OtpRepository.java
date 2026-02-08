package com.krowdless.repository;

import java.time.Instant;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.krowdless.entity.OtpEntity;
import com.krowdless.entity.UserEntity;
import com.krowdless.enums.OtpType;

@Repository
public interface OtpRepository extends JpaRepository<OtpEntity, Long> {

    // Get latest unused OTP for a user and type
    Optional<OtpEntity> findTopByUserAndTypeAndUsedFalseOrderByCreatedAtDesc(
            UserEntity user,
            OtpType type
    );

    // Delete expired OTPs (for cleanup jobs)
    void deleteByExpiresAtBefore(Instant time);

    // Optional: delete all OTPs for a user and type
    void deleteByUserAndType(UserEntity user, OtpType type);
}
