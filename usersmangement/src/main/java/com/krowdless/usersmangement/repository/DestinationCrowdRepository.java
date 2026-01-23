package com.krowdless.usersmangement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.krowdless.usersmangement.entity.DestinationCrowd;

public interface DestinationCrowdRepository
                extends JpaRepository<DestinationCrowd, Long> {

        List<DestinationCrowd> findByDestinationIdIn(List<Long> destinationIds);
}
