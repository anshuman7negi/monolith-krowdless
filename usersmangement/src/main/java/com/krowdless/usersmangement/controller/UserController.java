package com.krowdless.usersmangement.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.krowdless.usersmangement.dto.ApiResponse;
import com.krowdless.usersmangement.dto.LoginResponseDto;
import com.krowdless.usersmangement.dto.TopTravelerDto;
import com.krowdless.usersmangement.dto.UserLoginRequestDto;
import com.krowdless.usersmangement.dto.UserProfileDto;
import com.krowdless.usersmangement.dto.UserRegisterRequestDto;
import com.krowdless.usersmangement.dto.UserResponseDto;
import com.krowdless.usersmangement.entity.UserEntity;
import com.krowdless.usersmangement.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService service;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/ping")
    public String ping() {
        return "Users service OK";
    }

    // ✅ LOGIN
    @PostMapping("/login")
    public ApiResponse<LoginResponseDto> login(@Valid @RequestBody UserLoginRequestDto request) {
        logger.info("-------------------------------Method Entry: login-------------------------------------------");
        logger.error(
                "LOGIN DEBUG email={}, password={}",
                request.getEmail(),
                request.getPassword());

        LoginResponseDto responseDto = service.login(request.getEmail(), request.getPassword());

        if (responseDto == null) {
            return new ApiResponse<>("error", "Invalid username or password", null);
        }
        return new ApiResponse<>("success", "Login successful", responseDto);
    }

    // ✅ REGISTER
    @PostMapping("/register")
    public ApiResponse<UserResponseDto> register(@Valid @RequestBody UserRegisterRequestDto request) {
        UserResponseDto user = service.register(request);
        return new ApiResponse<>("success", "User registered successfully", user);
    }

    // ✅ REFRESH TOKEN
    @PostMapping("/refresh")
    public ApiResponse<LoginResponseDto> refresh(@RequestBody Map<String, String> body) {
        String refreshToken = body.get("refreshToken");
        LoginResponseDto refreshed = service.refresh(refreshToken);

        if (refreshed == null) {
            return new ApiResponse<>("error", "Invalid or expired refresh token", null);
        }
        return new ApiResponse<>("success", "Token refreshed", refreshed);
    }

    // ✅ LOGOUT
    @PostMapping("/logout")
    public ApiResponse<Void> logout(
            @RequestHeader(value = "Authorization", required = false) String auth,
            @RequestBody(required = false) Map<String, String> body) {

        String refreshToken = (body != null) ? body.get("refreshToken") : null;
        service.logout(auth, refreshToken);
        return new ApiResponse<>("success", "Logged out", null);
    }

    @PostMapping(value = "/me/profile-photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<UserResponseDto> uploadMyProfilePhoto(
            @AuthenticationPrincipal UserEntity user,
            @RequestPart("file") MultipartFile file) {
        UserResponseDto dto = service.uploadProfilePhoto(user.getId(), file);

        return new ApiResponse<>(
                "success",
                "Profile image uploaded",
                dto);
    }

    @GetMapping("/me/profile-photo")
    public ApiResponse<String> getMyProfilePhoto(
            @AuthenticationPrincipal UserEntity user) {
        String imageUrl = service.getProfilePhotoUrl(user.getId());
        return new ApiResponse<>("success", "Profile photo fetched", imageUrl);
    }

    @GetMapping("/me/profile")
    public ApiResponse<UserProfileDto> getMyProfile(
            @AuthenticationPrincipal UserEntity user) {
        return new ApiResponse<>(
                "success",
                "User profile loaded",
                service.getUserProfile(user.getId()));
    }

    @GetMapping("/me/leaderboard")
    public ApiResponse<Page<TopTravelerDto>> getLeaderboard(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return new ApiResponse<>(
                "success",
                "Top travelers loaded",
                service.getTopTravelers(page, size));
    }

}
