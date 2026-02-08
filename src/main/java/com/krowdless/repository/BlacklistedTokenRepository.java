package com.krowdless.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.krowdless.entity.BlacklistedTokenEntity;

public interface BlacklistedTokenRepository extends JpaRepository<BlacklistedTokenEntity, Long> {
    boolean existsByJti(String jti);
}
