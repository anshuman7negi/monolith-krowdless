package com.krowdless.usersmangement.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import com.krowdless.usersmangement.entity.*;
import com.krowdless.usersmangement.repository.*;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class StayApprovalService {

    private final StayDraftRepository stayDraftRepo;
    private final StayRepository stayRepo;

    private final StayDraftCapacityRepository draftCapacityRepo;
    private final StayDraftPricingRepository draftPricingRepo;
    private final StayDraftAmenityRepository draftAmenityRepo;
    private final StayDraftMediaRepository draftMediaRepo;

    private final StayCapacityRepository stayCapacityRepo;
    private final StayPricingRepository stayPricingRepo;
    private final StayAmenityRepository stayAmenityRepo;
    private final StayMediaRepository stayMediaRepo;

    private final StayAdminReviewRepository reviewRepo;

    public StayApprovalService(
            StayDraftRepository stayDraftRepo,
            StayRepository stayRepo,
            StayDraftCapacityRepository draftCapacityRepo,
            StayDraftPricingRepository draftPricingRepo,
            StayDraftAmenityRepository draftAmenityRepo,
            StayDraftMediaRepository draftMediaRepo,
            StayCapacityRepository stayCapacityRepo,
            StayPricingRepository stayPricingRepo,
            StayAmenityRepository stayAmenityRepo,
            StayMediaRepository stayMediaRepo,
            StayAdminReviewRepository reviewRepo) {

        this.stayDraftRepo = stayDraftRepo;
        this.stayRepo = stayRepo;
        this.draftCapacityRepo = draftCapacityRepo;
        this.draftPricingRepo = draftPricingRepo;
        this.draftAmenityRepo = draftAmenityRepo;
        this.draftMediaRepo = draftMediaRepo;
        this.stayCapacityRepo = stayCapacityRepo;
        this.stayPricingRepo = stayPricingRepo;
        this.stayAmenityRepo = stayAmenityRepo;
        this.stayMediaRepo = stayMediaRepo;
        this.reviewRepo = reviewRepo;
    }

    // =========================
    // APPROVE DRAFT
    // =========================
    @Transactional
    public Stay approveDraft(Long stayDraftId, Long adminUserId, String notes) {

        if (reviewRepo.existsByStayDraftId(stayDraftId)) {
            throw new RuntimeException("Draft already reviewed");
        }

        StayDraft draft = stayDraftRepo.findById(stayDraftId)
                .orElseThrow(() -> new RuntimeException("Draft not found"));

        if (!"PENDING_REVIEW".equals(draft.getStatus())) {
            throw new RuntimeException("Only pending drafts can be approved");
        }

        // 1️⃣ Update draft
        draft.setStatus("APPROVED");
        draft.setUpdatedAt(OffsetDateTime.now());
        stayDraftRepo.save(draft);

        // 2️⃣ Admin review
        StayAdminReview review = new StayAdminReview();
        review.setStayDraftId(draft.getId());
        review.setAdminUserId(adminUserId);
        review.setVideoCallDone(true);
        review.setVideoCallNotes(notes);
        review.setDecision("APPROVED");
        review.setDecisionAt(OffsetDateTime.now());
        reviewRepo.save(review);

        // 3️⃣ Create Stay (LIVE)
        Stay stay = new Stay();
        stay.setHostUserId(draft.getHostUserId());
        stay.setTitle(draft.getTitle());
        stay.setDescription(draft.getDescription());
        stay.setFullAddress(draft.getFullAddress());
        stay.setCountryId(draft.getCountryId());
        stay.setStateId(draft.getStateId());
        stay.setLatitude(draft.getLatitude());
        stay.setLongitude(draft.getLongitude());
        stay.setPropertyType(draft.getPropertyType());
        stay.setStatus("ACTIVE");
        stay.setCreatedAt(OffsetDateTime.now());

        stay = stayRepo.save(stay);

        // 4️⃣ COPY CAPACITY
        StayDraftCapacity dc = draftCapacityRepo.findById(draft.getId())
                .orElseThrow(() -> new RuntimeException("Draft capacity missing"));

        StayCapacity sc = new StayCapacity();
        sc.setStayId(stay.getId());
        sc.setMaxGuests(dc.getMaxGuests());
        sc.setBedrooms(dc.getBedrooms());
        sc.setBeds(dc.getBeds());
        sc.setBathrooms(dc.getBathrooms());
        stayCapacityRepo.save(sc);

        // 5️⃣ COPY PRICING
        StayDraftPricing dp = draftPricingRepo.findById(draft.getId())
                .orElseThrow(() -> new RuntimeException("Draft pricing missing"));

        StayPricing sp = new StayPricing();
        sp.setStayId(stay.getId());
        sp.setPricePerNight(dp.getPricePerNight());
        sp.setMinNights(dp.getMinNights());
        sp.setMaxNights(dp.getMaxNights());
        sp.setCheckInTime(dp.getCheckInTime());
        sp.setCheckOutTime(dp.getCheckOutTime());
        stayPricingRepo.save(sp);

        // 6️⃣ COPY AMENITIES
        List<StayDraftAmenity> draftAmenities = draftAmenityRepo.findByStayDraftId(draft.getId());

        for (StayDraftAmenity da : draftAmenities) {
            StayAmenity sa = new StayAmenity();
            sa.setStayId(stay.getId());
            sa.setAmenityCode(da.getAmenityCode());
            stayAmenityRepo.save(sa);
        }

        // 7️⃣ COPY MEDIA
        List<StayDraftMedia> draftMedia = draftMediaRepo.findByStayDraftId(draft.getId());

        for (StayDraftMedia dm : draftMedia) {
            StayMedia sm = new StayMedia();
            sm.setStayId(stay.getId());
            sm.setMediaType(dm.getMediaType());
            sm.setMediaUrl(dm.getMediaUrl());
            sm.setSortOrder(dm.getSortOrder());
            sm.setCreatedAt(dm.getCreatedAt());
            stayMediaRepo.save(sm);
        }

        return stay;
    }

    // =========================
    // REJECT DRAFT
    // =========================
    @Transactional
    public void rejectDraft(Long stayDraftId, Long adminUserId, String reason) {

        StayDraft draft = stayDraftRepo.findById(stayDraftId)
                .orElseThrow(() -> new RuntimeException("Draft not found"));

        if (!"PENDING_REVIEW".equals(draft.getStatus())) {
            throw new RuntimeException("Only pending drafts can be rejected");
        }

        draft.setStatus("REJECTED");
        draft.setRejectionReason(reason);
        draft.setUpdatedAt(OffsetDateTime.now());
        stayDraftRepo.save(draft);

        StayAdminReview review = new StayAdminReview();
        review.setStayDraftId(draft.getId());
        review.setAdminUserId(adminUserId);
        review.setVideoCallDone(true);
        review.setDecision("REJECTED");
        review.setVideoCallNotes(reason);
        review.setDecisionAt(OffsetDateTime.now());

        reviewRepo.save(review);
    }
}
