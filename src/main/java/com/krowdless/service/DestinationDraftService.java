package com.krowdless.service;

import com.krowdless.dto.DestinationDraftRequest;
import com.krowdless.entity.DestinationDraft;
import com.krowdless.entity.DestinationDraftImage;
import com.krowdless.repository.DestinationDraftImageRepository;
import com.krowdless.repository.DestinationDraftRepository;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class DestinationDraftService {

    private final SupabaseStorageService storageService;
    private final DestinationDraftRepository repository;
    private final DestinationDraftImageRepository draftImageRepository;

    public DestinationDraftService(
            SupabaseStorageService storageService,
            DestinationDraftRepository repository,
            DestinationDraftImageRepository draftImageRepository) {
        this.storageService = storageService;
        this.repository = repository;
        this.draftImageRepository = draftImageRepository;
    }

    // CREATE DRAFT
    public DestinationDraft createDraft(DestinationDraftRequest request, Long userId) {
        DestinationDraft draft = new DestinationDraft();
        draft.setStateId(request.getStateId());
        draft.setName(request.getName());
        draft.setShortDescription(request.getShortDescription());
        draft.setFullDescription(request.getFullDescription());
        draft.setAddress(request.getAddress());
        draft.setPincode(request.getPincode());
        draft.setLatitude(request.getLatitude());
        draft.setLongitude(request.getLongitude());
        draft.setYoutubeVideoUrl(request.getYoutubeVideoUrl());
        draft.setStatus("PENDING");
        draft.setCreatedBy(userId);

        return repository.save(draft);
    }

    // GET MY DRAFTS
    public List<DestinationDraft> getMyDrafts(Long userId) {
        return repository.findByCreatedBy(userId);
    }

    // GET SINGLE DRAFT (SECURE)
    public DestinationDraft getDraft(Long draftId, Long userId) {
        return repository.findByIdAndCreatedBy(draftId, userId)
                .orElseThrow(() -> new RuntimeException("Draft not found or unauthorized"));
    }

    // UPLOAD DRAFT IMAGE (SECURE)
    @Transactional
    public void uploadDraftImage(
            Long draftId,
            MultipartFile file,
            int sortOrder,
            Long userId) {
        DestinationDraft draft = repository.findByIdAndCreatedBy(draftId, userId)
                .orElseThrow(() -> new RuntimeException("Draft not found or unauthorized"));

        if (!"PENDING".equals(draft.getStatus())) {
            throw new RuntimeException("Cannot upload image to non-pending draft");
        }

        String imageUrl = storageService.uploadDestinationDraftImage(
                draftId, file, sortOrder);

        DestinationDraftImage image = new DestinationDraftImage();
        image.setDestinationDraftId(draftId);
        image.setImageUrl(imageUrl);
        image.setSortOrder(sortOrder);

        draftImageRepository.save(image);
    }

    public List<DestinationDraftImage> getDraftImages(
            Long draftId,
            Long userId) {
        // Ownership check
        repository.findByIdAndCreatedBy(draftId, userId)
                .orElseThrow(() -> new RuntimeException("Draft not found or unauthorized"));

        return draftImageRepository
                .findByDestinationDraftIdOrderBySortOrder(draftId);
    }

    @Transactional
    public void deleteDraft(Long draftId, Long userId) {

        DestinationDraft draft = repository.findById(draftId)
                .orElseThrow(() -> new RuntimeException("Draft not found"));

        // 🔐 owner check
        if (!draft.getCreatedBy().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }

        // ❌ approved not allowed
        if (!"PENDING".equals(draft.getStatus())) {
            throw new RuntimeException("Approved destination cannot be deleted");
        }

        // 1️⃣ fetch images
        List<DestinationDraftImage> images = draftImageRepository.findByDestinationDraftId(draftId);

        // 2️⃣ delete images from Supabase
        for (DestinationDraftImage img : images) {
            storageService.deleteDestinationDraftImage(img.getImageUrl());
        }

        // 3️⃣ delete image rows
        draftImageRepository.deleteByDestinationDraftId(draftId);

        // 4️⃣ delete draft row ✅ CORRECT REPO
        repository.delete(draft);
    }

}
