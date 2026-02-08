package com.krowdless.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.krowdless.entity.UserSubscription;

import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<UserSubscription, Long> {

    Optional<UserSubscription> findByUserIdAndIsActiveTrue(Long userId);
}

