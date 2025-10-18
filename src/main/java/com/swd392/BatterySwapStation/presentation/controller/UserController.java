package com.swd392.BatterySwapStation.presentation.controller;

import com.swd392.BatterySwapStation.application.common.response.ApiResponse;
import com.swd392.BatterySwapStation.application.model.UpdateProfileCommand;
import com.swd392.BatterySwapStation.application.useCase.profile.RetrieveProfileDetailsUseCase;
import com.swd392.BatterySwapStation.application.useCase.profile.UpdateProfileUseCase;
import com.swd392.BatterySwapStation.infrastructure.security.user.CustomUserDetails;
import com.swd392.BatterySwapStation.presentation.dto.request.UpdateProfileRequest;
import com.swd392.BatterySwapStation.presentation.dto.response.UserResponse;
import com.swd392.BatterySwapStation.presentation.mapper.ResponseMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api")
public class UserController {

    private final RetrieveProfileDetailsUseCase retrieveProfileDetailsUseCase;
    private final UpdateProfileUseCase updateProfileUseCase;

    public UserController(RetrieveProfileDetailsUseCase retrieveProfileDetailsUseCase,
                          UpdateProfileUseCase updateProfileUseCase) {
        this.retrieveProfileDetailsUseCase = retrieveProfileDetailsUseCase;
        this.updateProfileUseCase = updateProfileUseCase;
    }

    @GetMapping("/profile")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ApiResponse<UserResponse>> retrieveProfileDetails(@AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) throw new UsernameNotFoundException("User not found.");
        var profile = retrieveProfileDetailsUseCase.execute(userDetails.getUserId());
        var response = ResponseMapper.mapToUserResponse(profile);
        return ResponseEntity.ok(new ApiResponse<>("Profile retrieved successfully", response));
    }


    @PatchMapping("/profile")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ApiResponse<UserResponse>> updateProfile(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                   @Valid @RequestBody UpdateProfileRequest request) {
        if (userDetails == null) throw new UsernameNotFoundException("User not found.");
        var command = UpdateProfileCommand.builder()
                .userId(userDetails.getUserId())
                .email(request.getEmail())
                .phone(request.getPhone())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .identityNumber(request.getIdentityNumber())
                .dateOfBirth(request.getDateOfBirth())
                .avatarUrl(request.getAvatarUrl())
                .build();
        var updatedProfile = updateProfileUseCase.execute(command);
        var response = ResponseMapper.mapToUserResponse(updatedProfile);
        return ResponseEntity.ok(new ApiResponse<>("Profile updated successfully", response));
    }
}
