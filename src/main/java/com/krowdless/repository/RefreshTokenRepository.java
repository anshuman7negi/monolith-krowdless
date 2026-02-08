package com.krowdless.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.krowdless.entity.RefreshTokenEntity;
import com.krowdless.entity.UserEntity;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
    
    Optional<RefreshTokenEntity> findByTokenAndRevokedFalse(String token);
    
    void deleteByUserId(Long userId); // optional for rotation/cleanup
    
	Optional<RefreshTokenEntity> findByJti(String jti);
	
	@Modifying
	@Query("UPDATE RefreshTokenEntity t SET t.revoked = true WHERE t.user = :user")
	void revokeAllByUser(@Param("user") UserEntity user);

}
