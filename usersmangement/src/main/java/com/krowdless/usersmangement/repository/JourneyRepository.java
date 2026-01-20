package com.krowdless.usersmangement.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import com.krowdless.usersmangement.entity.JourneyEntity;

public interface JourneyRepository extends JpaRepository<JourneyEntity, Long> {

    @Query("""
        SELECT COUNT(DISTINCT j.destination.id)
        FROM JourneyEntity j
        WHERE j.user.id = :userId
          AND j.status = 'COMPLETED'
    """)
    Integer countVisitedDestinations(@Param("userId") Long userId);
}
