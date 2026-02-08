package com.krowdless.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.krowdless.entity.StayDraftAmenity;
import com.krowdless.entity.StayDraftAmenityId;

public interface StayDraftAmenityRepository
                extends JpaRepository<StayDraftAmenity, StayDraftAmenityId> {

    List<StayDraftAmenity> findByStayDraftId(Long stayDraftId);


}
