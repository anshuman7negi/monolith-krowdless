package com.krowdless.usersmangement.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.krowdless.usersmangement.entity.TravelPackage;

import java.util.List;

public interface PackageRepository extends JpaRepository<TravelPackage, Long> {

    List<TravelPackage> findByUserId(Long userId);

    long countByUserId(Long userId);
}

