package com.krowdless.usersmangement.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.krowdless.usersmangement.entity.PackageImage;

import java.util.List;

public interface PackageImageRepository extends JpaRepository<PackageImage, Long> {

    List<PackageImage> findByPkgId(Long packageId);
}

