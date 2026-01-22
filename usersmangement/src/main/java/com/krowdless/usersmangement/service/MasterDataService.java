package com.krowdless.usersmangement.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

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
}
