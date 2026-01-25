package com.krowdless.usersmangement.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import com.krowdless.usersmangement.dto.StayListDto;
import com.krowdless.usersmangement.entity.Stay;
import com.krowdless.usersmangement.entity.StayCapacity;
import com.krowdless.usersmangement.entity.StayMedia;
import com.krowdless.usersmangement.entity.StayPricing;
import com.krowdless.usersmangement.entity.StateEntity;
import com.krowdless.usersmangement.repository.StateRepository;
import com.krowdless.usersmangement.repository.StayCapacityRepository;
import com.krowdless.usersmangement.repository.StayMediaRepository;
import com.krowdless.usersmangement.repository.StayPricingRepository;
import com.krowdless.usersmangement.repository.StayRepository;
import com.krowdless.usersmangement.specification.StaySpecification;

@Service
public class StayService {

    private final StayRepository stayRepo;
    private final StayCapacityRepository capacityRepo;
    private final StayPricingRepository pricingRepo;
    private final StayMediaRepository mediaRepo;
    private final StateRepository stateRepo;

    public StayService(
            StayRepository stayRepo,
            StayCapacityRepository capacityRepo,
            StayPricingRepository pricingRepo,
            StayMediaRepository mediaRepo,
            StateRepository stateRepo) {

        this.stayRepo = stayRepo;
        this.capacityRepo = capacityRepo;
        this.pricingRepo = pricingRepo;
        this.mediaRepo = mediaRepo;
        this.stateRepo = stateRepo;
    }

    public Page<StayListDto> listStays(
            Long stateId,
            String propertyType,
            Integer guests,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            int page,
            int size) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Stay> staysPage = stayRepo.findAll(
                StaySpecification.filter(
                        stateId,
                        propertyType,
                        guests,
                        minPrice,
                        maxPrice),
                pageable);

        List<Stay> stays = staysPage.getContent();

        if (stays.isEmpty()) {
            return staysPage.map(s -> null);
        }

        // 🔑 stayIds
        List<Long> stayIds = stays.stream()
                .map(Stay::getId)
                .toList();

        // 🔹 capacity map
        Map<Long, StayCapacity> capacityMap =
                capacityRepo.findByStayIdIn(stayIds)
                        .stream()
                        .collect(Collectors.toMap(
                                StayCapacity::getStayId,
                                c -> c));

        // 🔹 pricing map
        Map<Long, StayPricing> pricingMap =
                pricingRepo.findByStayIdIn(stayIds)
                        .stream()
                        .collect(Collectors.toMap(
                                StayPricing::getStayId,
                                p -> p));

        // 🔹 cover image map (first IMAGE)
        Map<Long, String> coverImageMap =
                mediaRepo.findByStayIdInAndMediaTypeOrderBySortOrderAsc(
                                stayIds, "IMAGE")
                        .stream()
                        .collect(Collectors.toMap(
                                StayMedia::getStayId,
                                StayMedia::getMediaUrl,
                                (first, ignore) -> first
                        ));

        // 🔹 state map
        Map<Long, String> stateMap =
                stateRepo.findAll()
                        .stream()
                        .collect(Collectors.toMap(
                                StateEntity::getId,
                                StateEntity::getName));

        // 🔹 DTO mapping
        return staysPage.map(stay -> {

            StayListDto dto = new StayListDto();

            dto.setId(stay.getId());
            dto.setTitle(stay.getTitle());
            dto.setPropertyType(stay.getPropertyType());
            dto.setStateName(stateMap.get(stay.getStateId()));

            StayCapacity cap = capacityMap.get(stay.getId());
            if (cap != null) {
                dto.setMaxGuests(cap.getMaxGuests());
            }

            StayPricing price = pricingMap.get(stay.getId());
            if (price != null) {
                dto.setPricePerNight(price.getPricePerNight());
            }

            dto.setCoverImageUrl(
                    coverImageMap.get(stay.getId())
            );

            return dto;
        });
    }
}
