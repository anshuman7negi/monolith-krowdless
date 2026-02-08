package com.krowdless.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.krowdless.entity.StayDraftCapacity;

public interface StayDraftCapacityRepository
                extends JpaRepository<StayDraftCapacity, Long> {

        List<StayDraftCapacity> findByStayDraftIdIn(List<Long> stayDraftIds);
}
