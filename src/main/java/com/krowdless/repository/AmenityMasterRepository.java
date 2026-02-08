package com.krowdless.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.krowdless.entity.AmenityMaster;

public interface AmenityMasterRepository
        extends JpaRepository<AmenityMaster, String> {
}
