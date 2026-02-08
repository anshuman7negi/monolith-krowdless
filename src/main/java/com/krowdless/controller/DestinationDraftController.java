package com.krowdless.controller;

import com.krowdless.dto.DestinationDraftRequest;
import com.krowdless.entity.DestinationDraft;
import com.krowdless.entity.UserEntity;
import com.krowdless.service.DestinationDraftService;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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


    @PostMapping
    public DestinationDraft createDraft(@RequestBody DestinationDraftRequest request) {
    	Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        UserEntity user = (UserEntity) authentication.getPrincipal();

        Long userId = user.getId();
        return service.createDraft(request, userId);
    }

    @GetMapping("/my")
    public List<DestinationDraft> myDrafts() {
    	Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        UserEntity user = (UserEntity) authentication.getPrincipal();

        Long userId = user.getId();
        return service.getMyDrafts(userId);
    }

    @GetMapping("/{id}")
    public DestinationDraft getDraft(@PathVariable Long id) {
    	Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        UserEntity user = (UserEntity) authentication.getPrincipal();

        Long userId = user.getId();
        return service.getDraft(id, userId);
    }

    @PostMapping(value = "/{draftId}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void uploadDraftImage(
            @PathVariable Long draftId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("sortOrder") int sortOrder) {
    	Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        UserEntity user = (UserEntity) authentication.getPrincipal();

        Long userId = user.getId();
        service.uploadDraftImage(draftId, file, sortOrder, userId);
    }

    @GetMapping("/{draftId}/images")
    public List<?> getDraftImages(
            @PathVariable Long draftId) {
    	Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        UserEntity user = (UserEntity) authentication.getPrincipal();

        Long userId = user.getId();
        return service.getDraftImages(draftId, userId);
    }

    @DeleteMapping("/{draftId}")
    public void deleteDraft(@PathVariable Long draftId) {
    	Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        UserEntity user = (UserEntity) authentication.getPrincipal();

        Long userId = user.getId();
        service.deleteDraft(draftId, userId);
    }

}
