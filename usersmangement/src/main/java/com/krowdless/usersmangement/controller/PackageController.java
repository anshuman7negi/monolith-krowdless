package com.krowdless.usersmangement.controller;

import com.krowdless.usersmangement.dto.ApiResponse;
import com.krowdless.usersmangement.dto.PackageCreateRequest;
import com.krowdless.usersmangement.service.PackageService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/packages")
public class PackageController {

    private static final Logger log = LoggerFactory.getLogger(PackageController.class);

    private final PackageService packageService;

    public PackageController(PackageService packageService) {
        this.packageService = packageService;
    }

    @PostMapping(value = "/create-with-images", consumes = "multipart/form-data")
    public ApiResponse createPackageWithImages(
            @RequestPart("data") @Valid PackageCreateRequest request,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            Authentication authentication
    ) {

        Long userId = (Long) authentication.getPrincipal();

        log.info("📦 Creating package for userId={}, images={}",
                userId, images != null ? images.size() : 0);

        Long packageId =
                packageService.createPackageWithImages(userId, request, images);

        return new ApiResponse(
                "200",
                "Package created successfully",
                packageId
        );
    }
}
