package com.krowdless.usersmangement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.krowdless.usersmangement.entity.AmenityMaster;

public interface AmenityMasterRepository
        extends JpaRepository<AmenityMaster, String> {
}
