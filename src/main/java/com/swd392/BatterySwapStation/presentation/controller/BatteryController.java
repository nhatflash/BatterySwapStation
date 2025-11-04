package com.swd392.BatterySwapStation.presentation.controller;

import com.swd392.BatterySwapStation.application.common.response.ApiResponse;
import com.swd392.BatterySwapStation.application.model.AddNewBatteryCommand;
import com.swd392.BatterySwapStation.application.model.DefineBatteryModelCommand;
import com.swd392.BatterySwapStation.application.model.UpdateBatteryModelCommand;
import com.swd392.BatterySwapStation.application.model.ViewBatteryInventoryCommand;
import com.swd392.BatterySwapStation.application.useCase.battery.*;
import com.swd392.BatterySwapStation.application.useCase.stationStaff.ViewBatteryInventoryUseCase;
import com.swd392.BatterySwapStation.domain.enums.BatteryStatus;
import com.swd392.BatterySwapStation.infrastructure.security.user.CustomUserDetails;
import com.swd392.BatterySwapStation.presentation.dto.request.AddNewBatteryRequest;
import com.swd392.BatterySwapStation.presentation.dto.request.DefineBatteryModelRequest;
import com.swd392.BatterySwapStation.presentation.dto.request.UpdateBatteryModelRequest;
import com.swd392.BatterySwapStation.presentation.dto.response.BatteryModelResponse;
import com.swd392.BatterySwapStation.presentation.dto.response.BatteryResponse;
import com.swd392.BatterySwapStation.presentation.mapper.ResponseMapper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/battery")
@SecurityRequirement(name = "bearerAuth")
public class BatteryController {

    private final DefineBatteryModelUseCase defineBatteryModelUseCase;
    private final RetrieveAllModelsUseCase retrieveAllModelsUseCase;
    private final UpdateBatteryModelUseCase updateBatteryModelUseCase;
    private final AddNewBatteryUseCase addNewBatteryUseCase;
    private final RetrieveBatteryDetailsUseCase retrieveBatteryDetailsUseCase;
    private final RetrieveAllBatteriesUseCase retrieveAllBatteriesUseCase;
    private final ViewBatteryInventoryUseCase viewBatteryInventoryUseCase;


    @PostMapping("/model")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<BatteryModelResponse>> defineNewModel(@RequestBody @Valid DefineBatteryModelRequest request) {
        var command = DefineBatteryModelCommand.builder()
                .type(request.getType())
                .manufacturer(request.getManufacturer())
                .chemistry(request.getChemistry())
                .weightKg(request.getWeightKg())
                .warrantyMonths(request.getWarrantyMonths())
                .maxChargePowerKwh(request.getMaxChargePowerKwh())
                .minSohThreshold(request.getMinSohThreshold())
                .compatibleVehicles(request.getCompatibleVehicles())
                .build();
        var definedModel = defineBatteryModelUseCase.execute(command);
        var response = ResponseMapper.toBatteryModelResponse(definedModel);
        return ResponseEntity.ok(new ApiResponse<>("Battery model created successfully", response));
    }

    @GetMapping("/model")
    public ResponseEntity<ApiResponse<List<BatteryModelResponse>>> retrieveAllModels(@RequestParam Integer page) {
        var models = retrieveAllModelsUseCase.execute(page);
        var response = models.stream()
                .map(ResponseMapper::toBatteryModelResponse)
                .toList();
        return ResponseEntity.ok(new ApiResponse<>("Battery models retrieved successfully", response));
    }

    @PatchMapping("/model/{modelId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<BatteryModelResponse>> updateBatteryModel(@PathVariable UUID modelId,
                                                                                @RequestBody @Valid UpdateBatteryModelRequest request) {
        var command = UpdateBatteryModelCommand.builder()
                .modelId(modelId)
                .type(request.getType())
                .manufacturer(request.getManufacturer())
                .chemistry(request.getChemistry())
                .weightKg(request.getWeightKg())
                .warrantyMonths(request.getWarrantyMonths())
                .maxChargePowerKwh(request.getMaxChargePowerKwh())
                .minSohThreshold(request.getMinSohThreshold())
                .build();
        var updatedModel = updateBatteryModelUseCase.execute(command);
        var response = ResponseMapper.toBatteryModelResponse(updatedModel);
        return ResponseEntity.ok(new ApiResponse<>("Battery model updated successfully", response));
    }

    @PostMapping("/station/{stationId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<BatteryResponse>> addNewBattery(@PathVariable UUID stationId,
                                                                      @RequestBody @Valid AddNewBatteryRequest request) {
        var command = AddNewBatteryCommand.builder()
                .currentStationId(stationId)
                .serialNumber(request.getSerialNumber())
                .type(request.getType())
                .capacityKwh(request.getCapacityKwh())
                .manufactureDate(request.getManufactureDate())
                .warrantyExpiryDate(request.getWarrantyExpiryDate())
                .notes(request.getNotes())
                .rentalPrice(request.getRentalPrice())
                .build();
        var newBattery = addNewBatteryUseCase.execute(command);
        var response = ResponseMapper.mapToBatteryResponse(newBattery);
        return ResponseEntity.ok(new ApiResponse<>("New battery added successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<BatteryResponse>> retrieveBatteryDetails(@RequestParam UUID batteryId) {
        var battery = retrieveBatteryDetailsUseCase.execute(batteryId);
        var response = ResponseMapper.mapToBatteryResponse(battery);
        return ResponseEntity.ok(new ApiResponse<>("Battery details retrieved successfully", response));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<BatteryResponse>>> retrieveAllBatteries(@RequestParam Integer page) {
        var batteries = retrieveAllBatteriesUseCase.execute(page);
        var response = batteries.stream().map(ResponseMapper::mapToBatteryResponse).toList();
        return ResponseEntity.ok(new ApiResponse<>("Batteries retrieved successfully", response));
    }

    @GetMapping("/station/{currentStationId}/status")
    public ResponseEntity<ApiResponse<List<BatteryResponse>>> viewBatteryInventory(@PathVariable UUID currentStationId,
                                                                                   @RequestParam BatteryStatus status,
                                                                                   @RequestParam Integer page,
                                                                                   @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            throw new UsernameNotFoundException("User not found");
        }
        var command = ViewBatteryInventoryCommand.builder()
                .staffId(userDetails.getUserId())
                .batteryStatus(status)
                .pageIndex(page)
                .build();
        var batteries = viewBatteryInventoryUseCase.execute(command);
        var response = batteries.stream().map(ResponseMapper::mapToBatteryResponse).toList();
        return ResponseEntity.ok(new ApiResponse<>("Batteries with status " + status.name() + " retrieved successfully", response));
    }


}
