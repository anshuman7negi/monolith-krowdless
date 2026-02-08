package com.krowdless.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.krowdless.entity.StayPayment;

public interface StayPaymentRepository
                extends JpaRepository<StayPayment, Long> {

        Optional<StayPayment> findByTransactionId(String transactionId);

        Optional<StayPayment> findTopByBookingIdOrderByCreatedAtDesc(Long bookingId);
}
