package com.krowdless.usersmangement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.krowdless.usersmangement.entity.StayPayment;

public interface StayPaymentRepository
                extends JpaRepository<StayPayment, Long> {

        Optional<StayPayment> findTopByBookingIdOrderByCreatedAtDesc(Long bookingId);
}
