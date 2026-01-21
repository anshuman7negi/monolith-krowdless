package com.krowdless.usersmangement.controller;

import com.krowdless.usersmangement.dto.DestinationDraftRequest;
import com.krowdless.usersmangement.entity.DestinationDraft;
import com.krowdless.usersmangement.service.DestinationDraftService;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/destination-drafts")
public class DestinationDraftController {

    private final DestinationDraftService service;

    public DestinationDraftController(DestinationDraftService service) {
        this.service = service;
    }

    // ⚠️ TEMP: userId hardcoded (JWT baad me)
    private Long getLoggedInUserId() {
        return 1L;
    }

    @PostMapping
    public DestinationDraft createDraft(@RequestBody DestinationDraftRequest request) {
        return service.createDraft(request, getLoggedInUserId());
    }

    @GetMapping("/my")
    public List<DestinationDraft> myDrafts() {
        return service.getMyDrafts(getLoggedInUserId());
    }

    @GetMapping("/{id}")
    public DestinationDraft getDraft(@PathVariable Long id) {
        return service.getDraft(id, getLoggedInUserId());
    }

    @PostMapping(value = "/{draftId}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void uploadDraftImage(
            @PathVariable Long draftId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("sortOrder") int sortOrder) {
        service.uploadDraftImage(draftId, file, sortOrder, getLoggedInUserId());
    }

    @GetMapping("/{draftId}/images")
    public List<?> getDraftImages(
            @PathVariable Long draftId) {
        return service.getDraftImages(draftId, getLoggedInUserId());
    }

}
