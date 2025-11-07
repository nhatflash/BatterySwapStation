package com.swd392.BatterySwapStation.presentation.controller;

import com.swd392.BatterySwapStation.application.common.response.ApiResponse;
import com.swd392.BatterySwapStation.application.model.command.RetrieveUsersByRoleCommand;
import com.swd392.BatterySwapStation.application.model.command.UpdateProfileCommand;
import com.swd392.BatterySwapStation.application.useCase.profile.RetrieveProfileDetailsUseCase;
import com.swd392.BatterySwapStation.application.useCase.profile.UpdateProfileUseCase;
import com.swd392.BatterySwapStation.application.useCase.user.RetrieveAllUsersUseCase;
import com.swd392.BatterySwapStation.application.useCase.user.RetrieveUsersByRoleUseCase;
import com.swd392.BatterySwapStation.domain.enums.UserRole;
import com.swd392.BatterySwapStation.presentation.dto.request.UpdateProfileRequest;
import com.swd392.BatterySwapStation.application.model.response.UserResponse;
import com.swd392.BatterySwapStation.application.common.mapper.ResponseMapper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class UserController {

    private final RetrieveProfileDetailsUseCase retrieveProfileDetailsUseCase;
    private final UpdateProfileUseCase updateProfileUseCase;
    private final RetrieveAllUsersUseCase retrieveAllUsersUseCase;
    private final RetrieveUsersByRoleUseCase retrieveUsersByRoleUseCase;

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<UserResponse>> retrieveProfileDetails() {
        var profile = retrieveProfileDetailsUseCase.execute(null);
        var response = ResponseMapper.mapToUserResponse(profile);
        return ResponseEntity.ok(new ApiResponse<>("Profile retrieved successfully", response));
    }


    @PatchMapping("/profile")
    public ResponseEntity<ApiResponse<UserResponse>> updateProfile(@Valid @RequestBody UpdateProfileRequest request) {
        var command = UpdateProfileCommand.builder()
                .email(request.getEmail())
                .phone(request.getPhone())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .identityNumber(request.getIdentityNumber())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .avatarUrl(request.getAvatarUrl())
                .build();
        var updatedProfile = updateProfileUseCase.execute(command);
        var response = ResponseMapper.mapToUserResponse(updatedProfile);
        return ResponseEntity.ok(new ApiResponse<>("Profile updated successfully", response));
    }


    @GetMapping("/user/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers(@RequestParam Integer page) {
        var users = retrieveAllUsersUseCase.execute(page);
        var response = users.stream()
                .map(ResponseMapper::mapToUserResponse)
                .toList();
        return ResponseEntity.ok(new ApiResponse<>("Users retrieved successfully", response));
    }

    @GetMapping("/user/role/{role}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getUsersByRole(@PathVariable UserRole role, @RequestParam Integer page) {
        var command = RetrieveUsersByRoleCommand.builder()
                .page(page)
                .role(role)
                .build();
        var users =  retrieveUsersByRoleUseCase.execute(command);
        var response = users.stream().map(ResponseMapper::mapToUserResponse).toList();
        return ResponseEntity.ok(new ApiResponse<>("Users with role " + role.toString() + " retrieved successfully", response));
    }
}
