package com.krowdless.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.krowdless.entity.PackageLimit;

import java.util.Optional;

public interface PackageLimitRepository extends JpaRepository<PackageLimit, Long> {

    Optional<PackageLimit> findByUserId(Long userId);
}
