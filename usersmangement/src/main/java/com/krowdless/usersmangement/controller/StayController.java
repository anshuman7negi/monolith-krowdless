package com.krowdless.usersmangement.controller;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import com.krowdless.usersmangement.dto.StayDraftDetailDto;
import com.krowdless.usersmangement.dto.StayListDto;
import com.krowdless.usersmangement.service.StayService;

@RestController
@RequestMapping("/api/stays")
public class StayController {

    private final StayService stayService;

    public StayController(StayService stayService) {
        this.stayService = stayService;
    }

    // 🔥 AIRBNB STYLE LIST
    @GetMapping
    public Page<StayListDto> listStays(
            @RequestParam(required = false) Long stateId,
            @RequestParam(required = false) String propertyType,
            @RequestParam(required = false) Integer guests,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,

            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return stayService.listStays(
                stateId,
                propertyType,
                guests,
                minPrice,
                maxPrice,
                page,
                size);
    }

    @GetMapping("/{stayId}/detail")
    public StayDraftDetailDto getStayDetail(
            @PathVariable Long stayId) {

        return stayService.getStayDetail(stayId);
    }

}
