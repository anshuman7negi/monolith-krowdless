package com.krowdless.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.krowdless.entity.StayAmenity;
import com.krowdless.entity.StayAmenityId;

public interface StayAmenityRepository
        extends JpaRepository<StayAmenity, StayAmenityId> {}

