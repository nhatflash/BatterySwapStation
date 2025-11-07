package com.swd392.BatterySwapStation.presentation.controller;

import com.swd392.BatterySwapStation.application.common.response.ApiResponse;
import com.swd392.BatterySwapStation.application.enums.BatteryStatusReq;
import com.swd392.BatterySwapStation.application.model.command.AddNewBatteryCommand;
import com.swd392.BatterySwapStation.application.model.command.DefineBatteryModelCommand;
import com.swd392.BatterySwapStation.application.model.command.UpdateBatteryModelCommand;
import com.swd392.BatterySwapStation.application.model.command.ViewBatteryInventoryCommand;
import com.swd392.BatterySwapStation.application.useCase.battery.*;
import com.swd392.BatterySwapStation.application.useCase.stationStaff.ViewBatteryInventoryUseCase;
import com.swd392.BatterySwapStation.domain.enums.BatteryStatus;
import com.swd392.BatterySwapStation.presentation.dto.AddNewBatteryRequest;
import com.swd392.BatterySwapStation.presentation.dto.DefineBatteryModelRequest;
import com.swd392.BatterySwapStation.presentation.dto.UpdateBatteryModelRequest;
import com.swd392.BatterySwapStation.application.model.response.BatteryModelResponse;
import com.swd392.BatterySwapStation.application.model.response.BatteryResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
        var response = defineBatteryModelUseCase.execute(command);
        return ResponseEntity.ok(new ApiResponse<>("Battery model created successfully", response));
    }

    @GetMapping("/model")
    public ResponseEntity<ApiResponse<List<BatteryModelResponse>>> retrieveAllModels(@RequestParam Integer page) {
        var response = retrieveAllModelsUseCase.execute(page);
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
        var response = updateBatteryModelUseCase.execute(command);
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
        var response = addNewBatteryUseCase.execute(command);
        return ResponseEntity.ok(new ApiResponse<>("New battery added successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<BatteryResponse>> retrieveBatteryDetails(@RequestParam UUID batteryId) {
        var response = retrieveBatteryDetailsUseCase.execute(batteryId);
        return ResponseEntity.ok(new ApiResponse<>("Battery details retrieved successfully", response));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<BatteryResponse>>> retrieveAllBatteries(@RequestParam Integer page) {
        var response = retrieveAllBatteriesUseCase.execute(page);
        return ResponseEntity.ok(new ApiResponse<>("Batteries retrieved successfully", response));
    }

    @GetMapping("/station/status")
    public ResponseEntity<ApiResponse<List<BatteryResponse>>> viewBatteryInventory(@RequestParam BatteryStatusReq status,
                                                                                   @RequestParam Integer page) {
        var command = ViewBatteryInventoryCommand.builder()
                .batteryStatus(status)
                .pageIndex(page)
                .build();
        var response = viewBatteryInventoryUseCase.execute(command);
        return ResponseEntity.ok(new ApiResponse<>("Batteries with status " + status.name() + " retrieved successfully", response));
    }


}
