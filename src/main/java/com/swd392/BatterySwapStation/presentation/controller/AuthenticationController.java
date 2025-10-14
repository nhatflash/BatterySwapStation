package com.swd392.BatterySwapStation.presentation.controller;

import com.swd392.BatterySwapStation.application.common.response.ApiResponse;
import com.swd392.BatterySwapStation.application.useCase.authentication.LoginUseCase;
import com.swd392.BatterySwapStation.application.useCase.driver.RegisterDriverUseCase;
import com.swd392.BatterySwapStation.presentation.dto.request.LoginRequest;
import com.swd392.BatterySwapStation.presentation.dto.request.RegisterDriverRequest;
import com.swd392.BatterySwapStation.presentation.dto.response.LoginResponse;
import com.swd392.BatterySwapStation.presentation.dto.response.RegisterDriverResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final RegisterDriverUseCase registerDriverUseCase;
    private final LoginUseCase loginUseCase;

    public AuthenticationController(RegisterDriverUseCase registerDriverUseCase,
                                    LoginUseCase loginUseCase) {
        this.registerDriverUseCase = registerDriverUseCase;
        this.loginUseCase = loginUseCase;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterDriverResponse>> registerDriver(@RequestBody @Valid RegisterDriverRequest request) {
        var response = registerDriverUseCase.execute(request);
        return ResponseEntity.ok(new ApiResponse<>("Driver registered successfully.", response));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody @Valid LoginRequest request) {
        var response = loginUseCase.execute(request);
        return ResponseEntity.ok(new ApiResponse<>("Login successfully.", response));
    }
}
