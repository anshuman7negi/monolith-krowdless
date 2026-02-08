package com.krowdless.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.krowdless.entity.CountryEntity;

public interface CountryRepository extends JpaRepository<CountryEntity, Long> {
     List<CountryEntity> findByActiveTrueOrderByNameAsc();
}
