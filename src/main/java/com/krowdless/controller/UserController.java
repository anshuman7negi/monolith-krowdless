package com.krowdless.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.krowdless.dto.ApiResponse;
import com.krowdless.dto.UserResponseDto;
import com.krowdless.entity.UserEntity;
import com.krowdless.records.ChangePasswordRequestDto;
import com.krowdless.records.ForgotPasswordRequestDto;
import com.krowdless.records.LoginRequestDto;
import com.krowdless.records.LoginResponseDto;
import com.krowdless.records.MyProfileResponseDto;
import com.krowdless.records.RefreshTokenRequestDto;
import com.krowdless.records.ResetPasswordRequestDto;
import com.krowdless.records.UserRegisterRequestDto;
import com.krowdless.records.VerifyOtpRequestDto;
import com.krowdless.records.VerifyResetOtpRequestDto;
import com.krowdless.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {

	private final UserService userService;

	@PostMapping("/auth/register")
	public ResponseEntity<ApiResponse<Void>> register(@Valid @RequestBody UserRegisterRequestDto request) {
		userService.register(request);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ApiResponse<>(true, "Registration successful. Please verify your email.", null));
	}

	@PostMapping("/auth/verify-email")
	public ResponseEntity<ApiResponse<Void>> verifyEmail(@Valid @RequestBody VerifyOtpRequestDto request) {
		userService.verifyEmail(request);
		return ResponseEntity.ok(new ApiResponse<>(true, "Email verified successfully", null));
	}

	@PostMapping("/auth/login")
	public ResponseEntity<ApiResponse<LoginResponseDto>> login(@Valid @RequestBody LoginRequestDto request) {
		LoginResponseDto response = userService.login(request);
		return ResponseEntity.ok(new ApiResponse<>(true, "Login successful", response));
	}

	@PostMapping("/auth/forgot-password")
	public ResponseEntity<ApiResponse<Void>> sendResetOtp(@Valid @RequestBody ForgotPasswordRequestDto request) {
		userService.sendResetOtp(request);
		return ResponseEntity.ok(new ApiResponse<>(true, "If the account exists, an OTP has been sent", null));
	}

	@PostMapping("/auth/verify-reset-otp")
	public ResponseEntity<ApiResponse<Void>> verifyResetOtp(@Valid @RequestBody VerifyResetOtpRequestDto request) {
		userService.verifyResetOtp(request);
		return ResponseEntity.ok(new ApiResponse<>(true, "OTP verified successfully", null));
	}

	@PostMapping("/auth/reset-password")
	public ResponseEntity<ApiResponse<Void>> resetPassword(@Valid @RequestBody ResetPasswordRequestDto request) {
		userService.resetPassword(request);
		return ResponseEntity.ok(new ApiResponse<>(true, "Password reset successfully", null));
	}

	@PostMapping("/auth/change-password")
	public ResponseEntity<ApiResponse<Void>> changePassword(@Valid @RequestBody ChangePasswordRequestDto request) {
		userService.changePassword(request);
		return ResponseEntity.ok(new ApiResponse<>(true, "Password changed successfully", null));
	}

	@PostMapping("/user/refresh")
	public ResponseEntity<ApiResponse<LoginResponseDto>> refresh(@Valid @RequestBody RefreshTokenRequestDto request) {
		LoginResponseDto response = userService.refreshToken(request);
		return ResponseEntity.ok(new ApiResponse<>(true, "Token refreshed successfully", response));
	}

	@PostMapping("/user/logout")
	public ResponseEntity<ApiResponse<Void>> logout(@Valid @RequestBody RefreshTokenRequestDto request) {
		userService.logout(request);
		return ResponseEntity.ok(new ApiResponse<>(true, "Logged out successfully", null));
	}

	@PostMapping(value = "/user/me/profile-photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ApiResponse<UserResponseDto> uploadMyProfilePhoto(@AuthenticationPrincipal UserEntity user,
			@RequestPart("file") MultipartFile file) {
		UserResponseDto dto = userService.uploadProfilePhoto(user.getId(), file);

		return new ApiResponse<>(true, "Profile image uploaded", dto);
	}

	@GetMapping("/user/me/profile-photo")
	public ApiResponse<String> getMyProfilePhoto(@AuthenticationPrincipal UserEntity user) {
		String imageUrl = userService.getProfilePhotoUrl(user.getId());
		return new ApiResponse<>(true, "Profile photo fetched", imageUrl);
	}

	@GetMapping("/user/me")
	public ResponseEntity<ApiResponse<MyProfileResponseDto>> getMyProfile() {
		MyProfileResponseDto profile = userService.getMyProfile();
		return ResponseEntity.ok(new ApiResponse<>(true, "Profile fetched successfully", profile));
	}

}
