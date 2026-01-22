package com.krowdless.usersmangement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.krowdless.usersmangement.entity.CountryEntity;

public interface CountryRepository extends JpaRepository<CountryEntity, Long> {
     List<CountryEntity> findByActiveTrueOrderByNameAsc();
}
