package com.krowdless.usersmangement.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.krowdless.usersmangement.entity.RefreshTokenEntity;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
    
    Optional<RefreshTokenEntity> findByTokenAndRevokedFalse(String token);
    void deleteByUserId(Long userId); // optional for rotation/cleanup
}
