package com.krowdless.usersmangement.repository;

import com.krowdless.usersmangement.entity.DestinationImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DestinationImageRepository
        extends JpaRepository<DestinationImage, Long> {

    List<DestinationImage>
    findByDestinationIdOrderBySortOrderAsc(Long destinationId);
}

