package com.krowdless.usersmangement.controller;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.krowdless.usersmangement.dto.StayDraftDetailDto;
import com.krowdless.usersmangement.dto.StayDraftRequest;
import com.krowdless.usersmangement.dto.StayListDto;
import com.krowdless.usersmangement.entity.StayDraft;
import com.krowdless.usersmangement.service.StayDraftService;
import java.time.OffsetDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/stays/drafts")
public class StayDraftController {

    private static final Logger log = LoggerFactory.getLogger(StayDraftController.class);

    private final StayDraftService service;

    public StayDraftController(StayDraftService service) {
        this.service = service;
    }

    // CREATE
    @PostMapping("/create-with-media")
    public StayDraft createDraftWithMedia(
            @RequestPart("data") StayDraftRequest request,
            @RequestPart("images") List<MultipartFile> images,
            @RequestPart("video") MultipartFile video,
            @RequestParam Long hostUserId) {

        log.info("create-with-media called for hostUserId={}", hostUserId);
        log.info("📦 Images count={}, Video present={}",
                images != null ? images.size() : 0,
                video != null && !video.isEmpty());

        // 1️⃣ Create draft
        StayDraft draft = service.createDraft(request, hostUserId);

        // 2️⃣ Upload images (max 5)
        if (images != null) {
            if (images.size() > 5) {
                throw new RuntimeException("Maximum 5 images allowed");
            }

            for (MultipartFile image : images) {
                log.info("Uploading image: {}", image.getOriginalFilename());
                service.uploadDraftMedia(
                        draft.getId(),
                        image,
                        "IMAGE",
                        hostUserId);
            }
        }

        // 3️⃣ Upload video (mandatory)
        if (video == null || video.isEmpty()) {
            throw new RuntimeException("Video is mandatory");
        }

        log.info("Uploading video: {}", video.getOriginalFilename());
        service.uploadDraftMedia(
                draft.getId(),
                video,
                "VIDEO",
                hostUserId);

        log.info("✅ create-with-media completed for draftId={}", draft.getId());
        return draft;
    }

    // UPDATE
    @PutMapping("/{draftId}")
    public StayDraft updateDraft(
            @PathVariable Long draftId,
            @RequestParam Long hostUserId,
            @RequestBody StayDraftRequest request) {
        return service.updateDraft(draftId, hostUserId, request);
    }

    // HOST drafts
    @GetMapping("/my")
    public List<StayDraft> myDrafts(@RequestParam Long hostUserId) {
        return service.getMyDrafts(hostUserId);
    }

    // =========================
    // ADMIN – PENDING STAYS
    // =========================
    @GetMapping("/pending")
    public Page<StayListDto> listDraftStays(

            @RequestParam(required = false) String status,
            @RequestParam(required = false) OffsetDateTime fromDate,
            @RequestParam(required = false) OffsetDateTime toDate,

            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return service.listDraftStays(
                status,
                fromDate,
                toDate,
                page,
                size);
    }

    @GetMapping("/{draftId}/detail")
    public StayDraftDetailDto getDraftDetail(
            @PathVariable Long draftId,
            @RequestParam Long hostUserId) {

        return service.getDraftDetail(draftId, hostUserId);
    }

}
