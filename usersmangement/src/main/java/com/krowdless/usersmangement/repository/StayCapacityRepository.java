package com.krowdless.usersmangement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.krowdless.usersmangement.entity.StayCapacity;

public interface StayCapacityRepository
                extends JpaRepository<StayCapacity, Long> {

        List<StayCapacity> findByStayIdIn(List<Long> stayIds);
}
