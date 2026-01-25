package com.krowdless.usersmangement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.krowdless.usersmangement.entity.StayAdminReview;

import java.util.Optional;

public interface StayAdminReviewRepository extends JpaRepository<StayAdminReview, Long> {

    Optional<StayAdminReview> findByStayDraftId(Long stayDraftId);

    boolean existsByStayDraftId(Long stayDraftId);
}

