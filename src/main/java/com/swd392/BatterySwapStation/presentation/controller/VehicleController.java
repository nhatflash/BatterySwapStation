package com.swd392.BatterySwapStation.presentation.controller;

import com.swd392.BatterySwapStation.application.common.response.ApiResponse;
import com.swd392.BatterySwapStation.application.model.RegisterVehicleCommand;
import com.swd392.BatterySwapStation.application.model.UpdateVehicleCommand;
import com.swd392.BatterySwapStation.application.useCase.driver.GetDriverVehiclesUseCase;
import com.swd392.BatterySwapStation.application.useCase.driver.RegisterVehicleUseCase;
import com.swd392.BatterySwapStation.application.useCase.driver.UpdateVehicleUseCase;
import com.swd392.BatterySwapStation.application.useCase.vehicle.RetrieveUserVehiclesUseCase;
import com.swd392.BatterySwapStation.infrastructure.security.user.CustomUserDetails;
import com.swd392.BatterySwapStation.presentation.dto.request.RegisterVehicleRequest;
import com.swd392.BatterySwapStation.presentation.dto.request.UpdateVehicleRequest;
import com.swd392.BatterySwapStation.presentation.dto.response.VehicleResponse;
import com.swd392.BatterySwapStation.presentation.mapper.ResponseMapper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/vehicle")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class VehicleController {

    private final RegisterVehicleUseCase registerVehicleUseCase;
    private final GetDriverVehiclesUseCase getDriverVehiclesUseCase;
    private final UpdateVehicleUseCase updateVehicleUseCase;
    private final RetrieveUserVehiclesUseCase retrieveUserVehiclesUseCase;

    @PostMapping("/register")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<ApiResponse<VehicleResponse>> registerVehicle(@RequestBody @Valid RegisterVehicleRequest request,
                                                                        @AuthenticationPrincipal CustomUserDetails userDetails) {
        var command = RegisterVehicleCommand.builder()
                .userId(userDetails.getUserId())
                .vin(request.getVin())
                .make(request.getMake())
                .model(request.getModel())
                .year(request.getYear())
                .licensePlate(request.getLicensePlate())
                .batteryType(request.getBatteryType())
                .batteryCapacity(request.getBatteryCapacity())
                .build();
        var registeredVehicle = registerVehicleUseCase.execute(command);
        var response = ResponseMapper.toVehicleResponse(registeredVehicle);
        return ResponseEntity.ok(new ApiResponse<>("Register vehicle successfully.", response));
    }


    @GetMapping
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<ApiResponse<List<VehicleResponse>>> retrieveUserVehicles(@AuthenticationPrincipal CustomUserDetails userDetails) {
        var userVehicles = getDriverVehiclesUseCase.execute(userDetails.getUserId());
        var response = userVehicles.stream()
                .map(ResponseMapper::toVehicleResponse)
                .toList();
        return ResponseEntity.ok(new ApiResponse<>("Retrieve user vehicles successfully.", response));
    }

    @PatchMapping("/{vehicleId}")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<ApiResponse<VehicleResponse>> updateVehicle(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                       @Valid @RequestBody UpdateVehicleRequest request,
                                                                      @PathVariable UUID vehicleId) {
        var command = UpdateVehicleCommand.builder()
                .driverId(userDetails.getUserId())
                .vehicleId(vehicleId)
                .vin(request.getVin())
                .make(request.getMake())
                .model(request.getModel())
                .year(request.getYear())
                .licensePlate(request.getLicensePlate())
                .batteryType(request.getBatteryType())
                .batteryCapacity(request.getBatteryCapacity())
                .build();
        var updatedVehicle = updateVehicleUseCase.execute(command);
        var response = ResponseMapper.toVehicleResponse(updatedVehicle);
        return ResponseEntity.ok(new ApiResponse<>("Update vehicle successfully.", response));
    }

    @GetMapping("/all/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<ApiResponse<List<VehicleResponse>>> retrieveAllUserVehicles(@PathVariable UUID userId) {
        var vehicles = retrieveUserVehiclesUseCase.execute(userId);
        var response = vehicles.stream().map(ResponseMapper::toVehicleResponse).toList();
        return ResponseEntity.ok(new ApiResponse<>("Retrieve user vehicles successfully.", response));
    }


}
