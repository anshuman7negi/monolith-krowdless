package com.krowdless.usersmangement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.krowdless.usersmangement.entity.StayPricing;

public interface StayPricingRepository
                extends JpaRepository<StayPricing, Long> {

        List<StayPricing> findByStayIdIn(List<Long> stayIds);
}
