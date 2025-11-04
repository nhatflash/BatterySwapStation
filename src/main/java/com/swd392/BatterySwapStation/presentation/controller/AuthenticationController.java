package com.swd392.BatterySwapStation.presentation.controller;

import com.swd392.BatterySwapStation.application.common.response.ApiResponse;
import com.swd392.BatterySwapStation.application.model.LoginCommand;
import com.swd392.BatterySwapStation.application.model.LogoutCommand;
import com.swd392.BatterySwapStation.application.model.RegisterDriverCommand;
import com.swd392.BatterySwapStation.application.useCase.authentication.LoginUseCase;
import com.swd392.BatterySwapStation.application.useCase.authentication.LogoutAllUseCase;
import com.swd392.BatterySwapStation.application.useCase.authentication.LogoutUseCase;
import com.swd392.BatterySwapStation.application.useCase.driver.RegisterDriverUseCase;
import com.swd392.BatterySwapStation.infrastructure.security.user.CustomUserDetails;
import com.swd392.BatterySwapStation.infrastructure.service.TokenService;
import com.swd392.BatterySwapStation.presentation.dto.request.LoginRequest;
import com.swd392.BatterySwapStation.presentation.dto.request.RegisterDriverRequest;
import com.swd392.BatterySwapStation.presentation.dto.request.TokenRequest;
import com.swd392.BatterySwapStation.presentation.dto.response.LoginResponse;
import com.swd392.BatterySwapStation.presentation.dto.response.RegisterDriverResponse;
import com.swd392.BatterySwapStation.presentation.mapper.ResponseMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final RegisterDriverUseCase registerDriverUseCase;
    private final LoginUseCase loginUseCase;
    private final TokenService tokenService;
    private final LogoutUseCase logoutUseCase;
    private final LogoutAllUseCase logoutAllUseCase;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterDriverResponse>> registerDriver(@RequestBody @Valid RegisterDriverRequest request) {
        var command = RegisterDriverCommand.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .confirmPassword(request.getConfirmPassword())
                .phone(request.getPhone())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .dateOfBirth(request.getDateOfBirth())
                .identityNumber(request.getIdentityNumber())
                .build();
        var driver = registerDriverUseCase.execute(command);
        var response = ResponseMapper.toRegisterDriverResponse(driver);
        return ResponseEntity.ok(new ApiResponse<>("Driver registered successfully.", response));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody @Valid LoginRequest request) {
        var command = LoginCommand.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .build();
        var tokens = loginUseCase.execute(command);
        String accessToken = tokens.get("accessToken");
        String refreshToken = tokens.get("refreshToken");
        var response = ResponseMapper.toLoginResponse(accessToken, refreshToken);
        return ResponseEntity.ok(new ApiResponse<>("Login successfully.", response));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<String>> refreshAccessToken(@RequestBody TokenRequest request) {
        var response = tokenService.refreshAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(new ApiResponse<>("Refresh token successfully.", response));
    }


    @PostMapping("/logout")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ApiResponse<String>> logout(@RequestHeader("Authorization") String token,
                                                      @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) throw new UsernameNotFoundException("User not found.");
        String accessToken = token.substring(7);
        var command = LogoutCommand.builder()
                        .accessToken(accessToken)
                        .userId(userDetails.getUserId())
                        .build();
        logoutUseCase.execute(command);
        return ResponseEntity.ok(new ApiResponse<>("Logout successfully.", null));
    }

    @PostMapping("/logout-all")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ApiResponse<String>> logoutAll(@AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) throw new UsernameNotFoundException("User not found.");
        logoutAllUseCase.execute(userDetails.getUserId());
        return ResponseEntity.ok(new ApiResponse<>("Logout all successfully.", null));
    }
}
