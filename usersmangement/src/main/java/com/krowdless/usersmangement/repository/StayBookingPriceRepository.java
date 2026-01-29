package com.krowdless.usersmangement.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.krowdless.usersmangement.entity.StayBookingPrice;

public interface StayBookingPriceRepository
        extends JpaRepository<StayBookingPrice, Long> {
}

