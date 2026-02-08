package com.krowdless.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.krowdless.config.JwtService;
import com.krowdless.dto.UserResponseDto;
import com.krowdless.entity.RefreshTokenEntity;
import com.krowdless.entity.UserEntity;
import com.krowdless.enums.AccountStatus;
import com.krowdless.enums.OtpType;
import com.krowdless.enums.UserRole;
import com.krowdless.exception.BadRequestException;
import com.krowdless.exception.ForbiddenException;
import com.krowdless.exception.ResourceNotFoundException;
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
import com.krowdless.repository.RefreshTokenRepository;
import com.krowdless.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final SupabaseStorageService supabaseStorageService;
    private final OtpService otpService;
    
    @Transactional
    public void register(UserRegisterRequestDto dto) {

        // 1. Check email uniqueness
        if (userRepository.existsByEmail(dto.email().toLowerCase())) {
            throw new BadRequestException("Email already registered");
        }

        // 3. Create new user
        UserEntity user = UserEntity.builder()
                .username(dto.username())
                .email(dto.email().toLowerCase())
                .password(passwordEncoder.encode(dto.password()))
                .role(UserRole.ROLE_USER)             
                .status(AccountStatus.PENDING)        
                .emailVerified(false)
                .phoneVerified(false)
                .build();

        userRepository.save(user);

        // 4. Generate and send email OTP
        otpService.generateAndSendOtp(user, OtpType.EMAIL_VERIFICATION);
    }
    
    @Transactional
    public void verifyEmail(VerifyOtpRequestDto dto) {
    	UserEntity user = userRepository.findByEmail(dto.email()).orElseThrow(() -> new ResourceNotFoundException("User not Found"));
    	
    	otpService.verifyOtp(user, dto.otp(), OtpType.EMAIL_VERIFICATION);
    	
    	user.setEmailVerified(true);
    	user.setStatus(AccountStatus.ACTIVE);
    	userRepository.save(user);
    }
    
    @Transactional
    public LoginResponseDto login(LoginRequestDto request) {

        UserEntity user = findUserByIdentifier(request.identifier());

        if (user.getStatus() != AccountStatus.ACTIVE) {
            throw new ForbiddenException("Account is not active");
        }

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BadRequestException("Invalid credentials");
        }

        // Load UserDetails for JWT
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority(user.getRole().name()))
        );

        // 1. Generate access token
        String accessToken = jwtService.generateAccessToken(userDetails, user.getId());

        // 2. Generate jti
        String jti = UUID.randomUUID().toString();

        // 3. Generate refresh token
        String refreshToken = jwtService.generateRefreshToken(user.getEmail(), jti);

        // 4. Save refresh token in DB
        RefreshTokenEntity refreshEntity = new RefreshTokenEntity();
        refreshEntity.setUser(user);
        refreshEntity.setJti(jti);
        refreshEntity.setRevoked(false);
        refreshEntity.setExpiresAt(LocalDateTime.now().plusDays(7));

        refreshTokenRepository.save(refreshEntity);

        // 5. Return response
        return new LoginResponseDto(
                accessToken,
                refreshToken,
                user.getRole().name(),
                user.getId()
        );
    }
    
    @Transactional
    public void sendResetOtp(ForgotPasswordRequestDto request) {

        String identifier = request.identifier();

        UserEntity user = findUserByIdentifier(identifier);

        // Do not reveal user existence
        if (user == null) {
            return;
        }

        if (user.getStatus() != AccountStatus.ACTIVE) {
            throw new ForbiddenException("Account is not active");
        }

        // Decide OTP channel
        if (identifier.contains("@")) {
            otpService.generateAndSendOtp(user, OtpType.PASSWORD_RESET);
        } else {
            otpService.generateAndSendOtp(user, OtpType.PHONE_VERIFICATION);
        }
    }
    
    @Transactional
    public void verifyResetOtp(VerifyResetOtpRequestDto request) {

        // 1. Find user
        UserEntity user = findUserByIdentifier(request.identifier());

        if (user.getStatus() != AccountStatus.ACTIVE) {
            throw new ForbiddenException("Account is not active");
        }

        // 2. Determine OTP type
        OtpType type = request.identifier().contains("@")
                ? OtpType.PASSWORD_RESET
                : OtpType.PHONE_VERIFICATION;

        // 3. Verify OTP
        otpService.verifyOtp(user, request.otp(), type);
    }
    
    @Transactional
    public void resetPassword(ResetPasswordRequestDto request) {

        // 1. Check password match
        if (!request.newPassword().equals(request.confirmPassword())) {
            throw new BadRequestException("Passwords do not match");
        }

        // 2. Find user
        UserEntity user = findUserByIdentifier(request.identifier());

        if (user.getStatus() != AccountStatus.ACTIVE) {
            throw new ForbiddenException("Account is not active");
        }

        // 3. Update password (NO OTP check here)
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);

        // 4. Revoke all sessions
        refreshTokenRepository.revokeAllByUser(user);
    }
    
    @Transactional
    public void changePassword(ChangePasswordRequestDto request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        UserEntity user = (UserEntity) authentication.getPrincipal();

        // 1. Check current password
        if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
            throw new BadRequestException("Current password is incorrect");
        }

        // 2. Prevent same password reuse
        if (passwordEncoder.matches(request.newPassword(), user.getPassword())) {
            throw new BadRequestException("New password must be different from current password");
        }

        // 3. Update password
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }
    
    @Transactional
    public LoginResponseDto refreshToken(RefreshTokenRequestDto request) {

        String refreshToken = request.refreshToken();

        // 1. Extract jti from token
        String jti;
        try {
            jti = jwtService.extractJti(refreshToken);
        } catch (Exception e) {
            throw new ForbiddenException("Invalid refresh token");
        }

        // 2. Find token in DB
        RefreshTokenEntity storedToken = refreshTokenRepository.findByJti(jti)
                .orElseThrow(() ->
                        new ForbiddenException("Refresh token not recognized"));

        // 3. Check revoked
        if (storedToken.isRevoked()) {
            throw new ForbiddenException("Refresh token revoked");
        }

        // 4. Check expiry
        if (storedToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new ForbiddenException("Refresh token expired");
        }

        // 5. Get user
        UserEntity user = storedToken.getUser();

        if (user.getStatus() != AccountStatus.ACTIVE) {
            throw new ForbiddenException("Account is not active");
        }

        // 6. Revoke old token
        storedToken.setRevoked(true);
        refreshTokenRepository.save(storedToken);

        // 7. Build UserDetails
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority(user.getRole().name()))
        );

        // 8. Generate new access token
        String newAccessToken = jwtService.generateAccessToken(userDetails, user.getId());

        // 9. Generate new refresh token
        String newJti = UUID.randomUUID().toString();
        String newRefreshToken = jwtService.generateRefreshToken(user.getEmail(), newJti);

        // 10. Save new refresh token
        RefreshTokenEntity newEntity = new RefreshTokenEntity();
        newEntity.setUser(user);
        newEntity.setJti(newJti);
        newEntity.setRevoked(false);
        newEntity.setExpiresAt(LocalDateTime.now().plusDays(7));

        refreshTokenRepository.save(newEntity);

        // 11. Return response
        return new LoginResponseDto(
                newAccessToken,
                newRefreshToken,
                user.getRole().name(),
                user.getId()
        );
    }

    @Transactional
    public void logout(RefreshTokenRequestDto request) {

        String refreshToken = request.refreshToken();

        // 1. Extract jti
        String jti;
        try {
            jti = jwtService.extractJti(refreshToken);
        } catch (Exception e) {
            throw new ForbiddenException("Invalid refresh token");
        }

        // 2. Find token in DB
        RefreshTokenEntity storedToken = refreshTokenRepository.findByJti(jti)
                .orElseThrow(() ->
                        new ForbiddenException("Refresh token not recognized"));

        // 3. Revoke token
        storedToken.setRevoked(true);
        refreshTokenRepository.save(storedToken);
    }

    @Transactional
    public UserResponseDto uploadProfilePhoto(Long userId, MultipartFile file) {

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 1️⃣ Delete old image from bucket (if exists)
        if (user.getProfileImageUrl() != null) {
            supabaseStorageService.deleteUserProfile(user.getProfileImageUrl());
        }

        // 2️⃣ Upload new image
        String imageUrl = supabaseStorageService.uploadUserProfile(userId, file);

        // 3️⃣ Update DB
        user.setProfileImageUrl(imageUrl);
        userRepository.save(user);

        return toResponseDto(user);
    }

    public String getProfilePhotoUrl(Long userId) {

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getProfileImageUrl() == null) {
            throw new RuntimeException("Profile photo not uploaded");
        }

        return user.getProfileImageUrl();
    }

    @Transactional(readOnly = true)
    public MyProfileResponseDto getMyProfile() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        UserEntity user = (UserEntity) authentication.getPrincipal();

        return new MyProfileResponseDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPhone(),
                user.getRole().name(),
                user.getStatus().name(),
                user.isEmailVerified(),
                user.isPhoneVerified(),
                user.getProfileImageUrl()
        );
    }
    
    private UserResponseDto toResponseDto(UserEntity entity) {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(entity.getId());
        dto.setUsername(entity.getUsername());
        dto.setEmail(entity.getEmail());
        dto.setPhone(entity.getPhone());
        dto.setRole(entity.getRole().name());
        //dto.setVerified(entity.isVerified());
        return dto;
    }

    private UserEntity findUserByIdentifier(String identifier) {

        if (identifier == null || identifier.isBlank()) {
            throw new BadRequestException("Identifier is required");
        }

        // If contains '@', treat as email
        if (identifier.contains("@")) {
            return userRepository.findByEmail(identifier.toLowerCase())
                    .orElseThrow(() ->
                            new BadRequestException("Invalid credentials"));
        }

        // Otherwise treat as phone
        return userRepository.findByPhone(identifier)
                .orElseThrow(() ->
                        new BadRequestException("Invalid credentials"));
    }


}
