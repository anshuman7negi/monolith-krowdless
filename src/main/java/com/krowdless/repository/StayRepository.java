package com.krowdless.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.krowdless.entity.*;

import java.util.List;

public interface StayRepository extends JpaRepository<Stay, Long>,
        JpaSpecificationExecutor<Stay> {
    List<Stay> findByStatus(String status);
}
