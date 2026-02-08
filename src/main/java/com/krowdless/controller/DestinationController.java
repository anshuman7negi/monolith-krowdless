package com.krowdless.controller;

import com.krowdless.dto.DestinationDetailDto;
import com.krowdless.dto.DestinationListDto;
import com.krowdless.service.DestinationService;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/destinations")
public class DestinationController {

    private final DestinationService destinationService;

    public DestinationController(DestinationService destinationService) {
        this.destinationService = destinationService;
    }

@GetMapping
public Page<DestinationListDto> searchDestinations(
        @RequestParam(required = false) Long stateId,
        @RequestParam(required = false) String name,
        @RequestParam(required = false) Long categoryId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
) {
    return destinationService.searchDestinations(
            stateId,
            name,
            categoryId, 
            page,
            size
    );
}


     @GetMapping("/{id}")
    public DestinationDetailDto getDestinationDetail(
            @PathVariable Long id
    ) {
        return destinationService.getDestinationDetail(id);
    }
}
