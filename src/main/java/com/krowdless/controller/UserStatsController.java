package com.krowdless.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.krowdless.dto.ApiResponse;
import com.krowdless.dto.UserStatsDto;
import com.krowdless.service.UserStatsService;

@RestController
@RequestMapping("/users")
public class UserStatsController {

    @Autowired
    private UserStatsService service;

    @GetMapping("/{userId}/stats")
    public ApiResponse<UserStatsDto> getUserStats(
            @PathVariable Long userId) {

        return new ApiResponse<>(
        		true,
        "User stats loaded",
                service.getUserStats(userId)
        );
    }
}
