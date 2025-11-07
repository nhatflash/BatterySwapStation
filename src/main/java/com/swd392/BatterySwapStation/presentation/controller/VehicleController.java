package com.swd392.BatterySwapStation.presentation.controller;

import com.swd392.BatterySwapStation.application.common.response.ApiResponse;
import com.swd392.BatterySwapStation.application.model.command.RegisterVehicleCommand;
import com.swd392.BatterySwapStation.application.model.command.UpdateVehicleCommand;
import com.swd392.BatterySwapStation.application.useCase.driver.GetDriverVehiclesUseCase;
import com.swd392.BatterySwapStation.application.useCase.driver.RegisterVehicleUseCase;
import com.swd392.BatterySwapStation.application.useCase.driver.UpdateVehicleUseCase;
import com.swd392.BatterySwapStation.application.useCase.vehicle.RetrieveUserVehiclesUseCase;
import com.swd392.BatterySwapStation.presentation.dto.RegisterVehicleRequest;
import com.swd392.BatterySwapStation.presentation.dto.UpdateVehicleRequest;
import com.swd392.BatterySwapStation.application.model.response.VehicleResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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
    public ResponseEntity<ApiResponse<VehicleResponse>> registerVehicle(@RequestBody @Valid RegisterVehicleRequest request) {
        var command = RegisterVehicleCommand.builder()
                .vin(request.getVin())
                .make(request.getMake())
                .model(request.getModel())
                .year(request.getYear())
                .licensePlate(request.getLicensePlate())
                .batteryType(request.getBatteryType())
                .batteryCapacity(request.getBatteryCapacity())
                .build();
        var response = registerVehicleUseCase.execute(command);
        return ResponseEntity.ok(new ApiResponse<>("Register vehicle successfully.", response));
    }


    @GetMapping
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<ApiResponse<List<VehicleResponse>>> retrieveUserVehicles() {
        var response = getDriverVehiclesUseCase.execute(null);
        return ResponseEntity.ok(new ApiResponse<>("Retrieve user vehicles successfully.", response));
    }

    @PatchMapping("/{vehicleId}")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<ApiResponse<VehicleResponse>> updateVehicle(@Valid @RequestBody UpdateVehicleRequest request,
                                                                      @PathVariable UUID vehicleId) {
        var command = UpdateVehicleCommand.builder()
                .vehicleId(vehicleId)
                .vin(request.getVin())
                .make(request.getMake())
                .model(request.getModel())
                .year(request.getYear())
                .licensePlate(request.getLicensePlate())
                .batteryType(request.getBatteryType())
                .batteryCapacity(request.getBatteryCapacity())
                .build();
        var response = updateVehicleUseCase.execute(command);
        return ResponseEntity.ok(new ApiResponse<>("Update vehicle successfully.", response));
    }

    @GetMapping("/all/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<ApiResponse<List<VehicleResponse>>> retrieveAllUserVehicles(@PathVariable UUID userId) {
        var response = retrieveUserVehiclesUseCase.execute(userId);
        return ResponseEntity.ok(new ApiResponse<>("Retrieve user vehicles successfully.", response));
    }


}
