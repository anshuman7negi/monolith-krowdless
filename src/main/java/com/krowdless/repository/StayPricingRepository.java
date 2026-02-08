package com.krowdless.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.krowdless.entity.StayPricing;

public interface StayPricingRepository
                extends JpaRepository<StayPricing, Long> {

        List<StayPricing> findByStayIdIn(List<Long> stayIds);

}
