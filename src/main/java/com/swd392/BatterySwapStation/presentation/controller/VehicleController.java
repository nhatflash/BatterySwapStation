package com.swd392.BatterySwapStation.presentation.controller;

import com.swd392.BatterySwapStation.application.common.response.ApiResponse;
import com.swd392.BatterySwapStation.application.model.RegisterVehicleCommand;
import com.swd392.BatterySwapStation.application.useCase.driver.RegisterVehicleUseCase;
import com.swd392.BatterySwapStation.infrastructure.security.user.CustomUserDetails;
import com.swd392.BatterySwapStation.presentation.dto.request.RegisterVehicleRequest;
import com.swd392.BatterySwapStation.presentation.dto.response.RegisterVehicleResponse;
import com.swd392.BatterySwapStation.presentation.mapper.ResponseMapper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/vehicle")
@SecurityRequirement(name = "bearerAuth")
public class VehicleController {

    private final RegisterVehicleUseCase registerVehicleUseCase;

    public VehicleController(RegisterVehicleUseCase registerVehicleUseCase) {
        this.registerVehicleUseCase = registerVehicleUseCase;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterVehicleResponse>> registerVehicle(@RequestBody @Valid RegisterVehicleRequest request,
                                                                                @AuthenticationPrincipal CustomUserDetails userDetails) {
        var command = RegisterVehicleCommand.builder()
                .userId(userDetails.getUserId())
                .vin(request.getVin())
                .make(request.getMake())
                .model(request.getModel())
                .year(request.getYear())
                .licensePlate(request.getLicensePlate())
                .batteryType(request.getBatteryType())
                .build();
        var registeredVehicle = registerVehicleUseCase.execute(command);
        var response = ResponseMapper.toRegisterVehicleResponse(registeredVehicle);
        return ResponseEntity.ok(new ApiResponse<>("Register vehicle successfully.", response));
    }
}
