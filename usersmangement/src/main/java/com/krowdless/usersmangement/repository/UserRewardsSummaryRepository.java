package com.krowdless.usersmangement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.krowdless.usersmangement.entity.UserRewardsSummaryEntity;

public interface UserRewardsSummaryRepository
        extends JpaRepository<UserRewardsSummaryEntity, Long> {

    Optional<UserRewardsSummaryEntity> findByUserId(Long userId);
}
