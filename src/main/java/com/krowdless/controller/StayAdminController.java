package com.krowdless.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.krowdless.entity.Stay;
import com.krowdless.entity.UserEntity;
import com.krowdless.service.StayApprovalService;

@RestController
@RequestMapping("/api/admin/stays")
public class StayAdminController {

    private final StayApprovalService approvalService;

    public StayAdminController(StayApprovalService approvalService) {
        this.approvalService = approvalService;
    }

    // ✅ Approve draft → create live stay
    @PostMapping("/{stayDraftId}/approve")
    public Stay approveDraft(
            @PathVariable Long stayDraftId,
            @RequestParam(required = false) String notes
    ) {
    	Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        UserEntity user = (UserEntity) authentication.getPrincipal();

        Long userId = user.getId();
        return approvalService.approveDraft(stayDraftId, userId, notes);
    }

    // ❌ Reject draft
    @PostMapping("/{stayDraftId}/reject")
    public void rejectDraft(
            @PathVariable Long stayDraftId,
            @RequestParam Long adminUserId,
            @RequestParam String reason
    ) {
        approvalService.rejectDraft(stayDraftId, adminUserId, reason);
    }
}
