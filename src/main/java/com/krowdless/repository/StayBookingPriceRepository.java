package com.krowdless.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.krowdless.entity.StayBookingPrice;

public interface StayBookingPriceRepository
        extends JpaRepository<StayBookingPrice, Long> {
}

