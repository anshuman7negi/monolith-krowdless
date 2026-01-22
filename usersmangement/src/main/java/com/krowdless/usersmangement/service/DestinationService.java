package com.krowdless.usersmangement.service;

import com.krowdless.usersmangement.dto.DestinationDetailDto;
import com.krowdless.usersmangement.dto.DestinationListDto;
import com.krowdless.usersmangement.entity.Destination;
import com.krowdless.usersmangement.entity.StateEntity;
import com.krowdless.usersmangement.repository.DestinationImageRepository;
import com.krowdless.usersmangement.repository.DestinationRepository;
import com.krowdless.usersmangement.repository.StateRepository;
import com.krowdless.usersmangement.specification.DestinationSpecification;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
public class DestinationService {

        private final DestinationRepository destinationRepository;
        private final DestinationImageRepository destinationImageRepository;
        private final StateRepository stateRepository;

        public DestinationService(
                        DestinationRepository destinationRepository,
                        DestinationImageRepository destinationImageRepository,
                        StateRepository stateRepository) {
                this.destinationRepository = destinationRepository;
                this.destinationImageRepository = destinationImageRepository;
                this.stateRepository = stateRepository;
        }

        /*
         * =========================
         * LIST API
         * =========================
         */
        public Page<DestinationListDto> searchDestinations(
                        Long stateId,
                        String name,
                        int page,
                        int size) {

                Pageable pageable = PageRequest.of(
                                page,
                                size,
                                Sort.by(Sort.Direction.DESC, "createdAt"));

                Page<Destination> destinations = destinationRepository.findAll(
                                DestinationSpecification.filter(stateId, name),
                                pageable);

                Map<Long, String> stateMap = stateRepository.findByActiveTrueOrderByNameAsc()
                                .stream()
                                .collect(Collectors.toMap(
                                                StateEntity::getId,
                                                StateEntity::getName));

                return destinations.map(d -> {
                        DestinationListDto dto = new DestinationListDto();
                        dto.setId(d.getId());
                        dto.setName(d.getName());
                        dto.setShortDescription(d.getShortDescription());
                        dto.setCoverImageUrl(d.getCoverImageUrl());
                        dto.setStateName(stateMap.get(d.getStateId()));

                        dto.setRating(null);
                        dto.setPrice(null);
                        dto.setCrowdLevel("MEDIUM");

                        return dto;
                });
        }

        /*
         * =========================
         * DETAIL API
         * =========================
         */
        public DestinationDetailDto getDestinationDetail(Long destinationId) {

                Destination destination = destinationRepository
                                .findById(destinationId)
                                .filter(Destination::getActive)
                                .orElseThrow(() -> new RuntimeException("Destination not found"));

                DestinationDetailDto dto = new DestinationDetailDto();

                dto.setId(destination.getId());
                dto.setName(destination.getName());
                dto.setShortDescription(destination.getShortDescription());
                dto.setFullDescription(destination.getFullDescription());

                dto.setAddress(destination.getAddress());
                dto.setPincode(destination.getPincode());

                dto.setLatitude(
                                destination.getLatitude() != null
                                                ? destination.getLatitude().doubleValue()
                                                : null);
                dto.setLongitude(
                                destination.getLongitude() != null
                                                ? destination.getLongitude().doubleValue()
                                                : null);

                dto.setYoutubeVideoUrl(destination.getYoutubeVideoUrl());

                // ✅ ALL images for slider (cover included automatically)
                List<String> images = destinationImageRepository
                                .findByDestinationIdOrderBySortOrderAsc(destinationId)
                                .stream()
                                .map(img -> img.getImageUrl())
                                .toList();

                dto.setImages(images);

                dto.setTimings(List.of()); // later

                return dto;
        }

}
