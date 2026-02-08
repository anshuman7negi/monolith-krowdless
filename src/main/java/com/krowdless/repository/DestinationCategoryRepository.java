package com.krowdless.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.krowdless.entity.DestinationCategory;
import com.krowdless.entity.DestinationCategoryId;

public interface DestinationCategoryRepository
        extends JpaRepository<DestinationCategory, DestinationCategoryId> {

    List<DestinationCategory> findByIdDestinationIdIn(List<Long> destinationIds);

    List<DestinationCategory> findByIdDestinationId(Long destinationId);

    List<DestinationCategory> findByIdCategoryId(Long categoryId);
}
