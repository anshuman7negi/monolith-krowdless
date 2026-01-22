package com.krowdless.usersmangement.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import com.krowdless.usersmangement.entity.DestinationDraft;
import com.krowdless.usersmangement.service.AdminDestinationService;

@RestController
@RequestMapping("/admin/destinations")
public class AdminDestinationController {

    private final AdminDestinationService adminService;

    public AdminDestinationController(AdminDestinationService adminService) {
        this.adminService = adminService;
    }

    // =========================
    // GET ALL DRAFTS
    // =========================
    @GetMapping("/drafts")
    public Page<DestinationDraft> getDrafts(
            @RequestParam(defaultValue = "PENDING") String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return adminService.getDrafts(status, page, size);
    }

    // =========================
    // APPROVE DRAFT
    // =========================
    @PostMapping("/drafts/{id}/approve")
    public void approveDraft(@PathVariable Long id) {
        adminService.approveDraft(id, null); // adminId later
    }

    // =========================
    // REJECT DRAFT
    // =========================
    @PostMapping("/drafts/{id}/reject")
    public void rejectDraft(
            @PathVariable Long id,
            @RequestParam(required = false) String reason) {

        adminService.rejectDraft(id, reason);
    }
}
