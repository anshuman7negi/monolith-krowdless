package com.krowdless.usersmangement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.krowdless.usersmangement.dto.ApiResponse;
import com.krowdless.usersmangement.dto.UserStatsDto;
import com.krowdless.usersmangement.service.UserStatsService;

@RestController
@RequestMapping("/users")
public class UserStatsController {

    @Autowired
    private UserStatsService service;

    @GetMapping("/{userId}/stats")
    public ApiResponse<UserStatsDto> getUserStats(
            @PathVariable Long userId) {

        return new ApiResponse<>(
                "success",
        "User stats loaded",
                service.getUserStats(userId)
        );
    }
}
