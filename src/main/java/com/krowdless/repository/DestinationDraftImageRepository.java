package com.krowdless.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.krowdless.entity.DestinationDraftImage;

import java.util.List;
import java.util.Optional;

public interface DestinationDraftImageRepository
        extends JpaRepository<DestinationDraftImage, Long> {

    List<DestinationDraftImage>
        findByDestinationDraftIdOrderBySortOrder(Long destinationDraftId);

    List<DestinationDraftImage>
        findByDestinationDraftId(Long destinationDraftId);

    void deleteByDestinationDraftId(Long destinationDraftId);

        Optional<DestinationDraftImage>
        findFirstByDestinationDraftIdOrderBySortOrderAsc(Long draftId);


}
