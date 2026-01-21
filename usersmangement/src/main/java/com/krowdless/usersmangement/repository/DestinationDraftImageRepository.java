package com.krowdless.usersmangement.repository;

import com.krowdless.usersmangement.entity.DestinationDraftImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DestinationDraftImageRepository
        extends JpaRepository<DestinationDraftImage, Long> {

    List<DestinationDraftImage> findByDestinationDraftIdOrderBySortOrder(Long destinationDraftId);

    long countByDestinationDraftId(Long destinationDraftId);

    void deleteByDestinationDraftIdAndId(Long destinationDraftId, Long id);
}
