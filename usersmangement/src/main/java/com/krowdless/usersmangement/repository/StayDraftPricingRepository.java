package com.krowdless.usersmangement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.krowdless.usersmangement.entity.StayDraftPricing;

public interface StayDraftPricingRepository
                extends JpaRepository<StayDraftPricing, Long> {

        List<StayDraftPricing> findByStayDraftIdIn(List<Long> stayDraftIds);
}
