package com.krowdless.usersmangement.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.krowdless.usersmangement.entity.DestinationEntity;

public interface DestinationRepository extends JpaRepository<DestinationEntity, Long> {
    List<DestinationEntity> findByStateId(Long stateId);
}
