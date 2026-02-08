package com.krowdless.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.krowdless.entity.DestinationImage;

import java.util.List;

public interface DestinationImageRepository
        extends JpaRepository<DestinationImage, Long> {

    List<DestinationImage>
    findByDestinationIdOrderBySortOrderAsc(Long destinationId);
}

