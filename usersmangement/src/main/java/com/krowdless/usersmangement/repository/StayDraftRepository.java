package com.krowdless.usersmangement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.krowdless.usersmangement.entity.StayDraft;

import java.util.List;
import java.util.Optional;

public interface StayDraftRepository
        extends JpaRepository<StayDraft, Long>,
                JpaSpecificationExecutor<StayDraft> {

    Optional<StayDraft> findByIdAndHostUserId(Long id, Long hostUserId);

    List<StayDraft> findByHostUserId(Long hostUserId);

    List<StayDraft> findByStatus(String status);
}
