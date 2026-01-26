package com.krowdless.usersmangement.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.krowdless.usersmangement.dto.AmenityDto;
import com.krowdless.usersmangement.entity.*;
import com.krowdless.usersmangement.repository.*;

@Service
public class MasterDataService {

    @Autowired
    private CountryRepository countryRepo;

    @Autowired
    private StateRepository stateRepo;

    @Autowired
    private TitleRepository titleRepo;

    @Autowired
    private CategoryRepository categoryRepo;

    @Autowired
    private AmenityMasterRepository amenityMasterRepo;

    public List<CountryEntity> getCountries() {
        return countryRepo.findAll();
    }

    @Cacheable("states")
    public List<StateEntity> getStatesByCountry(Long countryId) {
        return stateRepo.findByCountryId(countryId);
    }

    public List<TitleEntity> getTitles() {
        return titleRepo.findAll();
    }

    @Cacheable("categories")
    public List<Category> getCategories() {
        return categoryRepo.findByActiveTrueOrderByNameAsc();
    }

    public List<AmenityDto> getAllAmenities() {
        return amenityMasterRepo.findAll()
                .stream()
                .map(a -> {
                    AmenityDto dto = new AmenityDto();
                    dto.setCode(a.getCode());
                    dto.setLabel(a.getLabel());
                    dto.setIcon(a.getIcon());
                    return dto;
                })
                .toList();
    }
}
