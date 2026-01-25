package com.krowdless.usersmangement.controller;

import org.springframework.web.bind.annotation.*;

import com.krowdless.usersmangement.entity.Stay;
import com.krowdless.usersmangement.service.StayApprovalService;

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
            @RequestParam Long adminUserId,
            @RequestParam(required = false) String notes
    ) {
        return approvalService.approveDraft(stayDraftId, adminUserId, notes);
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
