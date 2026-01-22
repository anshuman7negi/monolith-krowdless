package com.krowdless.usersmangement.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.krowdless.usersmangement.entity.Destination;
import com.krowdless.usersmangement.entity.DestinationDraft;
import com.krowdless.usersmangement.entity.DestinationDraftImage;
import com.krowdless.usersmangement.entity.DestinationImage;
import com.krowdless.usersmangement.repository.DestinationDraftImageRepository;
import com.krowdless.usersmangement.repository.DestinationDraftRepository;
import com.krowdless.usersmangement.repository.DestinationImageRepository;
import com.krowdless.usersmangement.repository.DestinationRepository;

import jakarta.transaction.Transactional;

@Service
public class AdminDestinationService {

    private final DestinationRepository destinationRepository;
    private final DestinationImageRepository destinationImageRepository;
    private final DestinationDraftRepository draftRepository;
    private final DestinationDraftImageRepository draftImageRepository;

    public AdminDestinationService(
            DestinationRepository destinationRepository,
            DestinationImageRepository destinationImageRepository,
            DestinationDraftRepository draftRepository,
            DestinationDraftImageRepository draftImageRepository) {

        this.destinationRepository = destinationRepository;
        this.destinationImageRepository = destinationImageRepository;
        this.draftRepository = draftRepository;
        this.draftImageRepository = draftImageRepository;
    }

    public Page<DestinationDraft> getDrafts(
            String status,
            int page,
            int size) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "createdAt"));

        // 🔹 status optional
        if (status == null || status.equalsIgnoreCase("ALL")) {
            return draftRepository.findAll(pageable);
        }

        return draftRepository.findByStatus(
                status.toUpperCase(),
                pageable);
    }

    // =========================
    // APPROVE DRAFT
    // =========================
    @Transactional
    public void approveDraft(Long draftId, Long adminId) {

        DestinationDraft draft = draftRepository.findById(draftId)
                .orElseThrow(() -> new RuntimeException("Draft not found"));

        if (!"PENDING".equals(draft.getStatus())) {
            throw new RuntimeException("Draft already processed");
        }

        // 1️⃣ Create destination
        Destination destination = new Destination();
        destination.setStateId(draft.getStateId());
        destination.setName(draft.getName());
        destination.setShortDescription(draft.getShortDescription());
        destination.setFullDescription(draft.getFullDescription());
        destination.setAddress(draft.getAddress());
        destination.setPincode(draft.getPincode());
        destination.setLatitude(
                draft.getLatitude() != null
                        ? BigDecimal.valueOf(draft.getLatitude())
                        : null);

        destination.setLongitude(
                draft.getLongitude() != null
                        ? BigDecimal.valueOf(draft.getLongitude())
                        : null);
        destination.setYoutubeVideoUrl(draft.getYoutubeVideoUrl());
        destination.setActive(true);

        destinationRepository.save(destination);

        // 2️⃣ Copy images
        List<DestinationDraftImage> draftImages = draftImageRepository.findByDestinationDraftId(draftId);

        boolean coverSet = false;

        for (DestinationDraftImage dImg : draftImages) {

            DestinationImage image = new DestinationImage();
            image.setDestinationId(destination.getId());
            image.setImageUrl(dImg.getImageUrl());
            image.setSortOrder(dImg.getSortOrder());

            destinationImageRepository.save(image);

            // 3️⃣ Auto cover = first image
            if (!coverSet) {
                destination.setCoverImageUrl(dImg.getImageUrl());
                coverSet = true;
            }
        }

        destinationRepository.save(destination);

        // 4️⃣ Update draft status
        draft.setStatus("APPROVED");
        draftRepository.save(draft);
    }

    // =========================
    // REJECT DRAFT
    // =========================
    @Transactional
    public void rejectDraft(Long draftId, String reason) {

        DestinationDraft draft = draftRepository.findById(draftId)
                .orElseThrow(() -> new RuntimeException("Draft not found"));

        if (!"PENDING".equals(draft.getStatus())) {
            throw new RuntimeException("Draft already processed");
        }

        // (optional: store reason later)
        draft.setStatus("REJECTED");
        draftRepository.save(draft);
    }
}
