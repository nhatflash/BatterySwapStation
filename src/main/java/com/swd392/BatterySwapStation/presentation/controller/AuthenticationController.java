package com.swd392.BatterySwapStation.presentation.controller;

import com.swd392.BatterySwapStation.application.common.response.ApiResponse;
import com.swd392.BatterySwapStation.application.model.command.LoginCommand;
import com.swd392.BatterySwapStation.application.model.command.LogoutCommand;
import com.swd392.BatterySwapStation.application.model.command.RegisterDriverCommand;
import com.swd392.BatterySwapStation.application.useCase.authentication.LoginUseCase;
import com.swd392.BatterySwapStation.application.useCase.authentication.LogoutAllUseCase;
import com.swd392.BatterySwapStation.application.useCase.authentication.LogoutUseCase;
import com.swd392.BatterySwapStation.application.useCase.authentication.RefreshTokenUseCase;
import com.swd392.BatterySwapStation.application.useCase.driver.RegisterDriverUseCase;
import com.swd392.BatterySwapStation.presentation.dto.LoginRequest;
import com.swd392.BatterySwapStation.presentation.dto.RegisterDriverRequest;
import com.swd392.BatterySwapStation.presentation.dto.TokenRequest;
import com.swd392.BatterySwapStation.application.model.response.LoginResponse;
import com.swd392.BatterySwapStation.application.model.response.RegisterDriverResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final RegisterDriverUseCase registerDriverUseCase;
    private final LoginUseCase loginUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;
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
        var response = registerDriverUseCase.execute(command);
        return ResponseEntity.ok(new ApiResponse<>("Driver registered successfully.", response));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody @Valid LoginRequest request) {
        var command = LoginCommand.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .build();
        var response = loginUseCase.execute(command);
        return ResponseEntity.ok(new ApiResponse<>("Login successfully.", response));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<String>> refreshAccessToken(@RequestBody TokenRequest request) {
        var response = refreshTokenUseCase.execute(request.getRefreshToken());
        return ResponseEntity.ok(new ApiResponse<>("Refresh token successfully.", response));
    }


    @PostMapping("/logout")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ApiResponse<String>> logout(@RequestHeader("Authorization") String token) {
        String accessToken = token.substring(7);
        var command = LogoutCommand.builder()
                        .accessToken(accessToken)
                        .build();
        logoutUseCase.execute(command);
        return ResponseEntity.ok(new ApiResponse<>("Logout successfully.", null));
    }

    @PostMapping("/logout-all")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ApiResponse<String>> logoutAll() {
        logoutAllUseCase.execute(null);
        return ResponseEntity.ok(new ApiResponse<>("Logout all successfully.", null));
    }
}
