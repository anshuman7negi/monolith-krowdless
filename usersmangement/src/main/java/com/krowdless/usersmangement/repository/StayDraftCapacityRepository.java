package com.krowdless.usersmangement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.krowdless.usersmangement.entity.StayDraftCapacity;

public interface StayDraftCapacityRepository
                extends JpaRepository<StayDraftCapacity, Long> {

        List<StayDraftCapacity> findByStayDraftIdIn(List<Long> stayDraftIds);
}
