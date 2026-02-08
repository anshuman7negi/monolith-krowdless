package com.krowdless.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.krowdless.dto.AmenityDto;
import com.krowdless.dto.ApiResponse;
import com.krowdless.entity.*;
import com.krowdless.service.MasterDataService;

@RestController
@RequestMapping("/master")
public class MasterDataController {

    @Autowired
    private MasterDataService service;

    // 🌍 Countries
    @GetMapping("/countries")
    public ApiResponse<List<CountryEntity>> getCountries() {
        return new ApiResponse<>(true, "Countries loaded", service.getCountries());
    }

    // 🗺 States by country
    @GetMapping("/states/{countryId}")
    public ApiResponse<List<StateEntity>> getStates(@PathVariable Long countryId) {
        return new ApiResponse<>(true, "States loaded", service.getStatesByCountry(countryId));
    }

    // 🏆 Titles
    @GetMapping("/titles")
    public ApiResponse<List<TitleEntity>> getTitles() {
        return new ApiResponse<>(true, "Titles loaded", service.getTitles());
    }

    @GetMapping("/categories")
    public ApiResponse<List<Category>> getCategories() {
        return new ApiResponse<>(true, "Categories loaded", service.getCategories());
    }

    @GetMapping("/amenities")
    public ApiResponse<List<AmenityDto>> getAllAmenities() {
         return new ApiResponse<>(true, "Categories loaded", service.getAllAmenities());
    }
}
