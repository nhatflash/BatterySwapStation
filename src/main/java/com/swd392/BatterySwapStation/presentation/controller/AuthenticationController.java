package com.swd392.BatterySwapStation.presentation.controller;

import com.swd392.BatterySwapStation.application.common.response.ApiResponse;
import com.swd392.BatterySwapStation.application.model.LoginCommand;
import com.swd392.BatterySwapStation.application.model.RegisterDriverCommand;
import com.swd392.BatterySwapStation.application.useCase.authentication.LoginUseCase;
import com.swd392.BatterySwapStation.application.useCase.driver.RegisterDriverUseCase;
import com.swd392.BatterySwapStation.presentation.dto.request.LoginRequest;
import com.swd392.BatterySwapStation.presentation.dto.request.RegisterDriverRequest;
import com.swd392.BatterySwapStation.presentation.dto.response.LoginResponse;
import com.swd392.BatterySwapStation.presentation.dto.response.RegisterDriverResponse;
import com.swd392.BatterySwapStation.presentation.mapper.ResponseMapper;
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
        var accessToken = loginUseCase.execute(command);
        var response = ResponseMapper.toLoginResponse(accessToken);
        return ResponseEntity.ok(new ApiResponse<>("Login successfully.", response));
    }
}
