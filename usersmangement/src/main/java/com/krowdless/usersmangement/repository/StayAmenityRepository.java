package com.krowdless.usersmangement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.krowdless.usersmangement.entity.StayAmenity;
import com.krowdless.usersmangement.entity.StayAmenityId;

public interface StayAmenityRepository
        extends JpaRepository<StayAmenity, StayAmenityId> {}

