package com.krowdless.usersmangement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.krowdless.usersmangement.entity.DestinationCategory;
import com.krowdless.usersmangement.entity.DestinationCategoryId;

public interface DestinationCategoryRepository
        extends JpaRepository<DestinationCategory, DestinationCategoryId> {

    List<DestinationCategory> findByIdDestinationIdIn(List<Long> destinationIds);

    List<DestinationCategory> findByIdDestinationId(Long destinationId);

    List<DestinationCategory> findByIdCategoryId(Long categoryId);
}
