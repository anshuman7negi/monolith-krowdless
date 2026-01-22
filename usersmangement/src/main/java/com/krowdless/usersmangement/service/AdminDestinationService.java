package com.krowdless.usersmangement.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.krowdless.usersmangement.dto.AdminDestinationDraftDetailDto;
import com.krowdless.usersmangement.dto.AdminDestinationDraftListDto;
import com.krowdless.usersmangement.entity.*;
import com.krowdless.usersmangement.repository.*;

import jakarta.transaction.Transactional;

@Service
public class AdminDestinationService {

    private final DestinationRepository destinationRepository;
    private final DestinationImageRepository destinationImageRepository;
    private final DestinationDraftRepository draftRepository;
    private final DestinationDraftImageRepository draftImageRepository;
    private final StateRepository stateRepository;
    private final UserRepository userRepository;

    public AdminDestinationService(
            DestinationRepository destinationRepository,
            DestinationImageRepository destinationImageRepository,
            DestinationDraftRepository draftRepository,
            DestinationDraftImageRepository draftImageRepository,
            StateRepository stateRepository,
            UserRepository userRepository) {

        this.destinationRepository = destinationRepository;
        this.destinationImageRepository = destinationImageRepository;
        this.draftRepository = draftRepository;
        this.draftImageRepository = draftImageRepository;
        this.stateRepository = stateRepository;
        this.userRepository = userRepository;
    }

    // =========================
    // LIST DRAFTS (ADMIN)
    // =========================
    public Page<AdminDestinationDraftListDto> getDrafts(
            String status,
            int page,
            int size) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<DestinationDraft> drafts =
                (status == null || status.equalsIgnoreCase("ALL"))
                        ? draftRepository.findAll(pageable)
                        : draftRepository.findByStatus(status.toUpperCase(), pageable);

        return drafts.map(draft -> {

            AdminDestinationDraftListDto dto = new AdminDestinationDraftListDto();

            dto.setId(draft.getId());
            dto.setName(draft.getName());
            dto.setShortDescription(draft.getShortDescription());
            dto.setStatus(draft.getStatus());
            dto.setCreatedAt(draft.getCreatedAt());

            // 🔹 state name
            stateRepository.findById(draft.getStateId())
                    .ifPresent(state -> dto.setStateName(state.getName()));

            // 🔹 cover image (first image)
            dto.setCoverImageUrl(
                    draftImageRepository
                            .findFirstByDestinationDraftIdOrderBySortOrderAsc(draft.getId())
                            .map(DestinationDraftImage::getImageUrl)
                            .orElse(null)
            );

            // 🔹 creator info
            userRepository.findById(draft.getCreatedBy())
                    .ifPresent(user -> {
                        dto.setCreatedByName(user.getUsername());
                        dto.setCreatedByEmail(user.getEmail());
                    });

            return dto;
        });
    }

    // =========================
    // DRAFT DETAIL (ADMIN)
    // =========================
    public AdminDestinationDraftDetailDto getDraftDetail(Long draftId) {

        DestinationDraft draft = draftRepository.findById(draftId)
                .orElseThrow(() -> new RuntimeException("Draft not found"));

        AdminDestinationDraftDetailDto dto = new AdminDestinationDraftDetailDto();

        dto.setId(draft.getId());
        dto.setName(draft.getName());
        dto.setShortDescription(draft.getShortDescription());
        dto.setFullDescription(draft.getFullDescription());
        dto.setAddress(draft.getAddress());
        dto.setPincode(draft.getPincode());
        dto.setLatitude(draft.getLatitude());
        dto.setLongitude(draft.getLongitude());
        dto.setYoutubeVideoUrl(draft.getYoutubeVideoUrl());
        dto.setStatus(draft.getStatus());
        dto.setAdminRemark(draft.getAdminRemark());
        dto.setCreatedAt(draft.getCreatedAt());
        dto.setReviewedAt(draft.getReviewedAt());

        dto.setStateId(draft.getStateId());

        stateRepository.findById(draft.getStateId())
                .ifPresent(state -> dto.setStateName(state.getName()));

        // 🔹 ALL images
        List<String> images =
                draftImageRepository
                        .findFirstByDestinationDraftIdOrderBySortOrderAsc(draftId)
                        .stream()
                        .map(DestinationDraftImage::getImageUrl)
                        .toList();

        dto.setImages(images);

        // 🔹 created by
        userRepository.findById(draft.getCreatedBy())
                .ifPresent(user -> {
                    dto.setCreatedByName(user.getUsername());
                    dto.setCreatedByEmail(user.getEmail());
                });

        // 🔹 reviewed by (future safe)
        if (draft.getReviewedBy() != null) {
            userRepository.findById(draft.getReviewedBy())
                    .ifPresent(user ->
                            dto.setReviewedByName(user.getUsername()));
        }

        return dto;
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

        // 🔹 copy images
        List<DestinationDraftImage> draftImages =
                draftImageRepository.findByDestinationDraftId(draftId);

        boolean coverSet = false;

        for (DestinationDraftImage dImg : draftImages) {

            DestinationImage image = new DestinationImage();
            image.setDestinationId(destination.getId());
            image.setImageUrl(dImg.getImageUrl());
            image.setSortOrder(dImg.getSortOrder());

            destinationImageRepository.save(image);

            if (!coverSet) {
                destination.setCoverImageUrl(dImg.getImageUrl());
                coverSet = true;
            }
        }

        destinationRepository.save(destination);

        draft.setStatus("APPROVED");
        draft.setReviewedBy(adminId);
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

        draft.setStatus("REJECTED");
        draft.setAdminRemark(reason);
        draftRepository.save(draft);
    }
}
