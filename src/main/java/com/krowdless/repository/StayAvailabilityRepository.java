package com.krowdless.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.krowdless.entity.StayAvailability;
import com.krowdless.entity.StayAvailabilityId;

public interface StayAvailabilityRepository
        extends JpaRepository<StayAvailability, StayAvailabilityId> {
}

