package com.krowdless.usersmangement.service;

import com.krowdless.usersmangement.dto.PackageCreateRequest;
import com.krowdless.usersmangement.entity.PackageImage;
import com.krowdless.usersmangement.entity.PackageLimit;
import com.krowdless.usersmangement.entity.TravelPackage;
import com.krowdless.usersmangement.repository.PackageLimitRepository;
import com.krowdless.usersmangement.repository.PackageRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class PackageService {

    private final PackageRepository packageRepository;
    private final PackageLimitRepository packageLimitRepository;
    private final SupabaseStorageService storageService;

    public PackageService(
            PackageRepository packageRepository,
            PackageLimitRepository packageLimitRepository,
            SupabaseStorageService storageService
    ) {
        this.packageRepository = packageRepository;
        this.packageLimitRepository = packageLimitRepository;
        this.storageService = storageService;
    }

    @Transactional
    public Long createPackageWithImages(
            Long userId,
            PackageCreateRequest request,
            List<MultipartFile> images
    ) {

        PackageLimit limit = packageLimitRepository
                .findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Package limit not initialized"));

        if (!limit.getIsSubscriptionActive()
                && limit.getTotalCreated() >= limit.getTotalAllowed()) {
            throw new RuntimeException("Free package limit reached");
        }

        if (images != null && images.size() > 5) {
            throw new RuntimeException("Maximum 5 images allowed");
        }

        // 1️⃣ Save package
        TravelPackage pkg = new TravelPackage();
        pkg.setUserId(userId);
        pkg.setTitle(request.getTitle());
        pkg.setDestination(request.getDestination());
        pkg.setPackageType(request.getPackageType());
        pkg.setDuration(request.getDuration());
        pkg.setStartingPrice(request.getStartingPrice());
        pkg.setDescription(request.getDescription());
        pkg.setCoverImageUrl(request.getCoverImageUrl());

        TravelPackage savedPackage =
                packageRepository.save(pkg);

        // 2️⃣ Upload images
        List<PackageImage> imageEntities = new ArrayList<>();

        if (images != null) {
            int order = 1;
            for (MultipartFile file : images) {

                String imageUrl =
                        storageService.uploadTravelPackageImage(
                                savedPackage.getId(),
                                file,
                                order++
                        );

                PackageImage img = new PackageImage();
                img.setPkg(savedPackage);
                img.setImageUrl(imageUrl);

                imageEntities.add(img);
            }
        }

        savedPackage.setImages(imageEntities);

        // 3️⃣ Update limit
        limit.setTotalCreated(limit.getTotalCreated() + 1);
        packageLimitRepository.save(limit);

        return savedPackage.getId();
    }
}
