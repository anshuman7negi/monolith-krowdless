package com.krowdless.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.krowdless.entity.DestinationDraft;

import java.util.List;
import java.util.Optional;

public interface DestinationDraftRepository
        extends JpaRepository<DestinationDraft, Long> {

    List<DestinationDraft> findByCreatedBy(Long createdBy);

    Optional<DestinationDraft> findByIdAndCreatedBy(Long id, Long createdBy);

    // 🔥 FILTER + PAGINATION
    Page<DestinationDraft> findByStatus(
            String status,
            Pageable pageable);
}
