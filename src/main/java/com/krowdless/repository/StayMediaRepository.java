package com.krowdless.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.krowdless.entity.StayMedia;

public interface StayMediaRepository
        extends JpaRepository<StayMedia, Long> {

    List<StayMedia> findByStayId(Long stayId);

    List<StayMedia> findByStayIdInAndMediaTypeOrderBySortOrderAsc(
            List<Long> stayIds,
            String mediaType);
  
}
