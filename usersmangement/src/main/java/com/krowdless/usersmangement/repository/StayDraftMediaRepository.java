package com.krowdless.usersmangement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.krowdless.usersmangement.entity.StayDraftMedia;

import java.util.List;

public interface StayDraftMediaRepository
        extends JpaRepository<StayDraftMedia, Long> {

    List<StayDraftMedia> findByStayDraftId(Long stayDraftId);

    List<StayDraftMedia> findByStayDraftIdInAndMediaTypeOrderBySortOrderAsc(
            List<Long> stayDraftIds,
            String mediaType);
}
