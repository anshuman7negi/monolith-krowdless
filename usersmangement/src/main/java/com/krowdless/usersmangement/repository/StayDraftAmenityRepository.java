package com.krowdless.usersmangement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.krowdless.usersmangement.entity.StayDraftAmenity;
import com.krowdless.usersmangement.entity.StayDraftAmenityId;

public interface StayDraftAmenityRepository
                extends JpaRepository<StayDraftAmenity, StayDraftAmenityId> {

        // ✅ CORRECT for EmbeddedId
    List<StayDraftAmenity> findByIdStayDraftId(Long stayDraftId);

        void deleteByIdStayDraftId(Long stayDraftId);
}
