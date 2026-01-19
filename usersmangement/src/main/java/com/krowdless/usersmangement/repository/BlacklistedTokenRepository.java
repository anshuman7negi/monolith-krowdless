package com.krowdless.usersmangement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.krowdless.usersmangement.entity.BlacklistedTokenEntity;

public interface BlacklistedTokenRepository extends JpaRepository<BlacklistedTokenEntity, Long> {
    boolean existsByJti(String jti);
}
