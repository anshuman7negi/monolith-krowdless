package com.krowdless.usersmangement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.krowdless.usersmangement.dto.ApiResponse;
import com.krowdless.usersmangement.entity.*;
import com.krowdless.usersmangement.service.MasterDataService;

@RestController
@RequestMapping("/master")
public class MasterDataController {

    @Autowired
    private MasterDataService service;

    // 🌍 Countries
    @GetMapping("/countries")
    public ApiResponse<List<CountryEntity>> getCountries() {
        return new ApiResponse<>("success", "Countries loaded", service.getCountries());
    }

    // 🗺 States by country
    @GetMapping("/states/{countryId}")
    public ApiResponse<List<StateEntity>> getStates(@PathVariable Long countryId) {
        return new ApiResponse<>("success", "States loaded", service.getStatesByCountry(countryId));
    }

    // 🏆 Titles
    @GetMapping("/titles")
    public ApiResponse<List<TitleEntity>> getTitles() {
        return new ApiResponse<>("success", "Titles loaded", service.getTitles());
    }

    @GetMapping("/categories")
    public ApiResponse<List<Category>> getCategories() {
        return new ApiResponse<>("success", "Categories loaded", service.getCategories());
    }
}
