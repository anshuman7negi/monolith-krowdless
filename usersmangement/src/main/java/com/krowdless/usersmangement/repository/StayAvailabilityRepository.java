package com.krowdless.usersmangement.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.krowdless.usersmangement.entity.StayAvailability;
import com.krowdless.usersmangement.entity.StayAvailabilityId;

public interface StayAvailabilityRepository
        extends JpaRepository<StayAvailability, StayAvailabilityId> {
}

