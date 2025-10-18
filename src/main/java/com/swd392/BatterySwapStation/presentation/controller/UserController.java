package com.swd392.BatterySwapStation.presentation.controller;

import com.swd392.BatterySwapStation.application.common.response.ApiResponse;
import com.swd392.BatterySwapStation.application.useCase.profile.RetrieveProfileDetailsUseCase;
import com.swd392.BatterySwapStation.infrastructure.security.user.CustomUserDetails;
import com.swd392.BatterySwapStation.presentation.dto.response.UserResponse;
import com.swd392.BatterySwapStation.presentation.mapper.ResponseMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api")
public class UserController {

    private final RetrieveProfileDetailsUseCase retrieveProfileDetailsUseCase;

    public UserController(RetrieveProfileDetailsUseCase retrieveProfileDetailsUseCase) {
        this.retrieveProfileDetailsUseCase = retrieveProfileDetailsUseCase;
    }

    @GetMapping("/profile")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ApiResponse<UserResponse>> retrieveProfileDetails(@AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) throw new UsernameNotFoundException("User not found.");
        var profile = retrieveProfileDetailsUseCase.execute(userDetails.getUserId());
        var response = ResponseMapper.mapToUserResponse(profile);
        return ResponseEntity.ok(new ApiResponse<>("Profile retrieved successfully", response));
    }
}
