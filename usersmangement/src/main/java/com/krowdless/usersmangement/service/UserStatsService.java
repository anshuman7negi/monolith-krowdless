package com.krowdless.usersmangement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.krowdless.usersmangement.dto.UserStatsDto;
import com.krowdless.usersmangement.entity.UserEntity;
import com.krowdless.usersmangement.repository.FollowerRepository;
import com.krowdless.usersmangement.repository.JourneyRepository;
import com.krowdless.usersmangement.repository.TitleRepository;
import com.krowdless.usersmangement.repository.UserRepository;
import com.krowdless.usersmangement.repository.UserRewardsSummaryRepository;

@Service
public class UserStatsService {

    @Autowired private UserRepository userRepository;
    @Autowired private FollowerRepository followerRepository;
    @Autowired private JourneyRepository journeyRepository;
    @Autowired private UserRewardsSummaryRepository rewardsSummaryRepository;
    @Autowired private TitleRepository userTitleRepository;

    public UserStatsDto getUserStats(Long userId) {

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserStatsDto dto = new UserStatsDto();
        dto.setUserId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setProfileImageUrl(user.getProfileImageUrl());

        dto.setFollowersCount(
                followerRepository.countFollowers(userId)
        );

        dto.setTotalPlacesVisited(
                journeyRepository.countVisitedDestinations(userId)
        );

        rewardsSummaryRepository.findByUserId(userId)
                .ifPresent(rs -> {
                    dto.setTotalPoints(rs.getTotalPoints());
                    dto.setTotalSpent(rs.getTotalSpent());
                });

        userTitleRepository.findLatestTitle(userId)
                .stream()
                .findFirst()
                .ifPresent(t -> {
                    dto.setTitleName(t.getName());
                });

        return dto;
    }
}

