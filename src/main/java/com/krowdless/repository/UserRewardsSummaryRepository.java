package com.krowdless.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.krowdless.entity.UserRewardsSummaryEntity;

public interface UserRewardsSummaryRepository
        extends JpaRepository<UserRewardsSummaryEntity, Long> {

    Optional<UserRewardsSummaryEntity> findByUserId(Long userId);
}
