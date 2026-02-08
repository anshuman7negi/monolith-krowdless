package com.krowdless.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import com.krowdless.entity.FollowerEntity;

public interface FollowerRepository extends JpaRepository<FollowerEntity, Long> {

    @Query("SELECT COUNT(f) FROM FollowerEntity f WHERE f.followingId = :userId")
    Long countFollowers(@Param("userId") Long userId);
}
