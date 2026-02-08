package com.krowdless.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.krowdless.entity.StateEntity;

public interface StateRepository extends JpaRepository<StateEntity, Long> {
    List<StateEntity> findByCountryId(Long countryId);
     List<StateEntity> findByActiveTrueOrderByNameAsc();
}
