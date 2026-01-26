package com.krowdless.usersmangement.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.krowdless.usersmangement.dto.AmenityDto;
import com.krowdless.usersmangement.dto.StayDraftDetailDto;
import com.krowdless.usersmangement.dto.StayDraftRequest;
import com.krowdless.usersmangement.dto.StayListDto;
import com.krowdless.usersmangement.entity.StayDraft;
import com.krowdless.usersmangement.entity.StayDraftAmenity;
import com.krowdless.usersmangement.entity.StayDraftCapacity;
import com.krowdless.usersmangement.entity.StayDraftMedia;
import com.krowdless.usersmangement.entity.StayDraftPricing;
import com.krowdless.usersmangement.repository.AmenityMasterRepository;
import com.krowdless.usersmangement.repository.StayDraftAmenityRepository;
import com.krowdless.usersmangement.repository.StayDraftCapacityRepository;
import com.krowdless.usersmangement.repository.StayDraftMediaRepository;
import com.krowdless.usersmangement.repository.StayDraftPricingRepository;
import com.krowdless.usersmangement.repository.StayDraftRepository;
import com.krowdless.usersmangement.specification.StayDraftSpecification;
import jakarta.transaction.Transactional;

import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StayDraftService {

    private final StayDraftRepository repository;
    private final StayDraftMediaRepository mediaRepository;
    private final StayDraftCapacityRepository capacityRepo;
    private final StayDraftPricingRepository pricingRepo;
    private final SupabaseStorageService storageService;
    private final StayDraftAmenityRepository stayDraftAmenityRepo;
    private final AmenityMasterRepository amenityMasterRepo;

    public StayDraftService(
            StayDraftRepository repository,
            StayDraftMediaRepository mediaRepository,
            StayDraftCapacityRepository capacityRepo,
            StayDraftPricingRepository pricingRepo,
            StayDraftAmenityRepository stayDraftAmenityRepo,
            AmenityMasterRepository amenityMasterRepo,
            SupabaseStorageService storageService) {

        this.repository = repository;
        this.mediaRepository = mediaRepository;
        this.capacityRepo = capacityRepo;
        this.pricingRepo = pricingRepo;
        this.stayDraftAmenityRepo = stayDraftAmenityRepo;
        this.amenityMasterRepo = amenityMasterRepo;
        this.storageService = storageService;
    }

    // =========================
    // CREATE DRAFT
    // =========================
    @Transactional
    public StayDraft createDraft(StayDraftRequest request, Long hostUserId) {

        StayDraft draft = new StayDraft();
        draft.setHostUserId(hostUserId);
        draft.setTitle(request.getTitle());
        draft.setDescription(request.getDescription());
        draft.setFullAddress(request.getFullAddress());
        draft.setCountryId(request.getCountryId());
        draft.setStateId(request.getStateId());
        draft.setLatitude(request.getLatitude());
        draft.setLongitude(request.getLongitude());
        draft.setPropertyType(request.getPropertyType());
        draft.setStatus("PENDING_REVIEW");
        draft.setCreatedAt(OffsetDateTime.now());
        draft.setUpdatedAt(OffsetDateTime.now());

        repository.save(draft);

        // capacity
        StayDraftCapacity capacity = new StayDraftCapacity();
        capacity.setStayDraftId(draft.getId());
        capacity.setMaxGuests(request.getMaxGuests());
        capacity.setBedrooms(request.getBedrooms());
        capacity.setBeds(request.getBeds());
        capacity.setBathrooms(request.getBathrooms());
        capacityRepo.save(capacity);

        // pricing
        StayDraftPricing pricing = new StayDraftPricing();
        pricing.setStayDraftId(draft.getId());
        pricing.setPricePerNight(request.getPricePerNight());
        pricing.setMinNights(1);
        pricing.setMaxNights(30);
        pricingRepo.save(pricing);

        return draft;
    }

    // =========================
    // UPDATE DRAFT
    // =========================
    public StayDraft updateDraft(
            Long draftId,
            Long hostUserId,
            StayDraftRequest request) {

        StayDraft draft = repository
                .findByIdAndHostUserId(draftId, hostUserId)
                .orElseThrow(() -> new RuntimeException("Draft not found or unauthorized"));

        if (!"PENDING_REVIEW".equals(draft.getStatus())) {
            throw new RuntimeException("Only pending drafts can be updated");
        }

        draft.setTitle(request.getTitle());
        draft.setDescription(request.getDescription());
        draft.setFullAddress(request.getFullAddress());
        draft.setCountryId(request.getCountryId());
        draft.setStateId(request.getStateId());
        draft.setLatitude(request.getLatitude());
        draft.setLongitude(request.getLongitude());
        draft.setPropertyType(request.getPropertyType());
        draft.setUpdatedAt(OffsetDateTime.now());

        return repository.save(draft);
    }

    // =========================
    // GET MY DRAFTS
    // =========================
    public List<StayDraft> getMyDrafts(Long hostUserId) {
        return repository.findByHostUserId(hostUserId);
    }

    // =========================
    // ADMIN – PENDING DRAFTS
    // =========================
    // =========================
    // ADMIN – LIST DRAFT STAYS
    // =========================
    public Page<StayListDto> listDraftStays(
            String status,
            OffsetDateTime fromDate,
            OffsetDateTime toDate,
            int page,
            int size) {

        // 🔥 default status
        if (status == null || status.isBlank()) {
            status = "PENDING_REVIEW";
        }

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<StayDraft> draftsPage = repository.findAll(
                StayDraftSpecification.filter(
                        status,
                        fromDate,
                        toDate),
                pageable);

        List<StayDraft> drafts = draftsPage.getContent();

        if (drafts.isEmpty()) {
            return draftsPage.map(d -> null);
        }

        List<Long> draftIds = drafts.stream()
                .map(StayDraft::getId)
                .toList();

        // 🔹 capacity
        Map<Long, StayDraftCapacity> capacityMap = capacityRepo.findByStayDraftIdIn(draftIds)
                .stream()
                .collect(Collectors.toMap(
                        StayDraftCapacity::getStayDraftId,
                        c -> c));

        // 🔹 pricing
        Map<Long, StayDraftPricing> pricingMap = pricingRepo.findByStayDraftIdIn(draftIds)
                .stream()
                .collect(Collectors.toMap(
                        StayDraftPricing::getStayDraftId,
                        p -> p));

        // 🔹 cover image
        Map<Long, String> coverImageMap = mediaRepository
                .findByStayDraftIdInAndMediaTypeOrderBySortOrderAsc(
                        draftIds, "IMAGE")
                .stream()
                .collect(Collectors.toMap(
                        StayDraftMedia::getStayDraftId,
                        StayDraftMedia::getMediaUrl,
                        (first, ignore) -> first));

        // 🔹 DTO MAP
        return draftsPage.map(draft -> {

            StayListDto dto = new StayListDto();

            dto.setId(draft.getId());
            dto.setTitle(draft.getTitle());
            dto.setPropertyType(draft.getPropertyType());
            dto.setStateName(
                    draft.getStateId() != null
                            ? String.valueOf(draft.getStateId())
                            : null);

            StayDraftCapacity cap = capacityMap.get(draft.getId());
            if (cap != null) {
                dto.setMaxGuests(cap.getMaxGuests());
            }

            StayDraftPricing price = pricingMap.get(draft.getId());
            if (price != null) {
                dto.setPricePerNight(price.getPricePerNight());
            }

            dto.setCoverImageUrl(
                    coverImageMap.get(draft.getId()));

            return dto;
        });
    }

    // =========================
    // UPLOAD MEDIA (IMAGE / VIDEO)
    // =========================
    @Transactional
    public void uploadDraftMedia(
            Long draftId,
            MultipartFile file,
            String mediaType,
            Long hostUserId) {
        // 1️⃣ Draft + ownership check
        StayDraft draft = repository
                .findByIdAndHostUserId(draftId, hostUserId)
                .orElseThrow(() -> new RuntimeException("Draft not found or unauthorized"));

        if (!"PENDING_REVIEW".equals(draft.getStatus())) {
            throw new RuntimeException("Cannot upload media to non-pending draft");
        }

        if (!"IMAGE".equals(mediaType) && !"VIDEO".equals(mediaType)) {
            throw new RuntimeException("Invalid media type");
        }

        // 2️⃣ Count existing media
        List<StayDraftMedia> existingMedia = mediaRepository.findByStayDraftId(draftId);

        long imageCount = existingMedia.stream()
                .filter(m -> "IMAGE".equals(m.getMediaType()))
                .count();

        long videoCount = existingMedia.stream()
                .filter(m -> "VIDEO".equals(m.getMediaType()))
                .count();

        // 3️⃣ Business rules
        if ("IMAGE".equals(mediaType) && imageCount >= 5) {
            throw new RuntimeException("Maximum 5 images allowed");
        }

        if ("VIDEO".equals(mediaType) && videoCount >= 1) {
            throw new RuntimeException("Only 1 video is allowed");
        }

        // 4️⃣ Upload to Supabase
        String mediaUrl = storageService.uploadStayDraftMedia(draftId, file, mediaType);

        // 5️⃣ Save DB record
        StayDraftMedia media = new StayDraftMedia();
        media.setStayDraftId(draftId);
        media.setMediaType(mediaType);
        media.setMediaUrl(mediaUrl);
        media.setCreatedAt(OffsetDateTime.now());

        mediaRepository.save(media);
    }

    // =========================
    // VALIDATE BEFORE SUBMIT (ADMIN QUEUE)
    // =========================
    public void validateBeforeSubmit(Long draftId, Long hostUserId) {

        StayDraft draft = repository
                .findByIdAndHostUserId(draftId, hostUserId)
                .orElseThrow(() -> new RuntimeException("Draft not found or unauthorized"));

        List<StayDraftMedia> mediaList = mediaRepository.findByStayDraftId(draftId);

        long imageCount = mediaList.stream()
                .filter(m -> "IMAGE".equals(m.getMediaType()))
                .count();

        long videoCount = mediaList.stream()
                .filter(m -> "VIDEO".equals(m.getMediaType()))
                .count();

        if (imageCount == 0) {
            throw new RuntimeException("At least 1 image is required");
        }

        if (videoCount != 1) {
            throw new RuntimeException("Exactly 1 video is mandatory");
        }
    }

    public StayDraftDetailDto getDraftDetail(Long draftId, Long hostUserId) {

        StayDraft draft = repository
                .findByIdAndHostUserId(draftId, hostUserId)
                .orElseThrow(() -> new RuntimeException("Draft not found or unauthorized"));

        StayDraftDetailDto dto = new StayDraftDetailDto();

        // ================= BASIC =================
        dto.setId(draft.getId());
        dto.setTitle(draft.getTitle());
        dto.setDescription(draft.getDescription());
        dto.setFullAddress(draft.getFullAddress());
        dto.setPropertyType(draft.getPropertyType());
        dto.setStatus(draft.getStatus());

        // ================= CAPACITY =================
        StayDraftCapacity cap = capacityRepo
                .findById(draftId)
                .orElse(null);

        if (cap != null) {
            dto.setMaxGuests(cap.getMaxGuests());
            dto.setBedrooms(cap.getBedrooms());
            dto.setBeds(cap.getBeds());
            dto.setBathrooms(cap.getBathrooms());
        }

        // ================= PRICING =================
        StayDraftPricing price = pricingRepo
                .findById(draftId)
                .orElse(null);

        if (price != null) {
            dto.setPricePerNight(price.getPricePerNight());
            dto.setMinNights(price.getMinNights());
            dto.setMaxNights(price.getMaxNights());
        }

        // ================= MEDIA =================
       List<StayDraftMedia> mediaList =
        mediaRepository.findByStayDraftId(draftId)
                .stream()
                .sorted(Comparator.comparing(
                        StayDraftMedia::getSortOrder,
                        Comparator.nullsLast(Integer::compareTo)))
                .toList();


        dto.setImageUrls(
                mediaList.stream()
                        .filter(m -> "IMAGE".equals(m.getMediaType()))
                        .map(StayDraftMedia::getMediaUrl)
                        .toList());

        dto.setVideoUrl(
                mediaList.stream()
                        .filter(m -> "VIDEO".equals(m.getMediaType()))
                        .map(StayDraftMedia::getMediaUrl)
                        .findFirst()
                        .orElse(null));

        // ================= AMENITIES =================
        var amenityCodes = stayDraftAmenityRepo.findByStayDraftId(draftId)
                .stream()
                .map(StayDraftAmenity::getAmenityCode)
                .toList();

        dto.setAmenities(
                amenityMasterRepo.findAllById(amenityCodes)
                        .stream()
                        .map(a -> {
                            AmenityDto ad = new AmenityDto();
                            ad.setCode(a.getCode());
                            ad.setLabel(a.getLabel());
                            ad.setIcon(a.getIcon());
                            return ad;
                        })
                        .toList());

        return dto;
    }

}
