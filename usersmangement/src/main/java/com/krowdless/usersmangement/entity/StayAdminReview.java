package com.krowdless.usersmangement.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "stay_admin_review")
public class StayAdminReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔑 FK to stay_draft(id)
    @Column(name = "stay_draft_id", nullable = false, unique = true)
    private Long stayDraftId;

    @Column(name = "admin_user_id")
    private Long adminUserId;

    @Column(name = "video_call_done")
    private Boolean videoCallDone = false;

    @Column(name = "video_call_notes")
    private String videoCallNotes;

    // APPROVED / REJECTED
    @Column(name = "decision")
    private String decision;

    @Column(name = "decision_at")
    private OffsetDateTime decisionAt;

    /* =========================
       GETTERS & SETTERS
       ========================= */

    public Long getId() {
        return id;
    }

    public Long getStayDraftId() {
        return stayDraftId;
    }

    public void setStayDraftId(Long stayDraftId) {
        this.stayDraftId = stayDraftId;
    }

    public Long getAdminUserId() {
        return adminUserId;
    }

    public void setAdminUserId(Long adminUserId) {
        this.adminUserId = adminUserId;
    }

    public Boolean getVideoCallDone() {
        return videoCallDone;
    }

    public void setVideoCallDone(Boolean videoCallDone) {
        this.videoCallDone = videoCallDone;
    }

    public String getVideoCallNotes() {
        return videoCallNotes;
    }

    public void setVideoCallNotes(String videoCallNotes) {
        this.videoCallNotes = videoCallNotes;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public OffsetDateTime getDecisionAt() {
        return decisionAt;
    }

    public void setDecisionAt(OffsetDateTime decisionAt) {
        this.decisionAt = decisionAt;
    }
}
