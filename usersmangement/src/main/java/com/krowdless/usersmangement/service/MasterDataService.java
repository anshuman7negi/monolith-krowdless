package com.krowdless.usersmangement.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
    private DestinationRepository destinationRepo;

    @Autowired
    private TitleRepository titleRepo;

    public List<CountryEntity> getCountries() {
        return countryRepo.findAll();
    }

    public List<StateEntity> getStatesByCountry(Long countryId) {
        return stateRepo.findByCountryId(countryId);
    }

    public List<DestinationEntity> getDestinationsByState(Long stateId) {
        return destinationRepo.findByStateId(stateId);
    }

    public List<TitleEntity> getTitles() {
        return titleRepo.findAll();
    }
}
