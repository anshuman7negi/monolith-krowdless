package com.krowdless.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.krowdless.entity.DestinationCrowd;

public interface DestinationCrowdRepository
                extends JpaRepository<DestinationCrowd, Long> {

        List<DestinationCrowd> findByDestinationIdIn(List<Long> destinationIds);
}
