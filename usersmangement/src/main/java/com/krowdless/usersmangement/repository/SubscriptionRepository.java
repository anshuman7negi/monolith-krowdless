package com.krowdless.usersmangement.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.krowdless.usersmangement.entity.UserSubscription;

import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<UserSubscription, Long> {

    Optional<UserSubscription> findByUserIdAndIsActiveTrue(Long userId);
}

