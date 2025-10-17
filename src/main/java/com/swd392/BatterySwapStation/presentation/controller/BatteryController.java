package com.swd392.BatterySwapStation.presentation.controller;

import com.swd392.BatterySwapStation.application.common.response.ApiResponse;
import com.swd392.BatterySwapStation.application.model.AddNewBatteryCommand;
import com.swd392.BatterySwapStation.application.model.DefineBatteryModelCommand;
import com.swd392.BatterySwapStation.application.model.UpdateBatteryModelCommand;
import com.swd392.BatterySwapStation.application.service.BatteryService;
import com.swd392.BatterySwapStation.application.useCase.battery.AddNewBatteryUseCase;
import com.swd392.BatterySwapStation.application.useCase.battery.DefineBatteryModelUseCase;
import com.swd392.BatterySwapStation.application.useCase.battery.RetrieveAllModelsUseCase;
import com.swd392.BatterySwapStation.application.useCase.battery.UpdateBatteryModelUseCase;
import com.swd392.BatterySwapStation.presentation.dto.request.AddNewBatteryRequest;
import com.swd392.BatterySwapStation.presentation.dto.request.DefineBatteryModelRequest;
import com.swd392.BatterySwapStation.presentation.dto.request.UpdateBatteryModelRequest;
import com.swd392.BatterySwapStation.presentation.dto.response.BatteryModelResponse;
import com.swd392.BatterySwapStation.presentation.dto.response.BatteryResponse;
import com.swd392.BatterySwapStation.presentation.mapper.ResponseMapper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.executable.ValidateOnExecution;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/battery")
@SecurityRequirement(name = "bearerAuth")
public class BatteryController {

    private final DefineBatteryModelUseCase defineBatteryModelUseCase;
    private final RetrieveAllModelsUseCase retrieveAllModelsUseCase;
    private final UpdateBatteryModelUseCase updateBatteryModelUseCase;
    private final AddNewBatteryUseCase addNewBatteryUseCase;

    public BatteryController(DefineBatteryModelUseCase defineBatteryModelUseCase,
                             RetrieveAllModelsUseCase retrieveAllModelsUseCase,
                             UpdateBatteryModelUseCase updateBatteryModelUseCase,
                             AddNewBatteryUseCase addNewBatteryUseCase) {
        this.defineBatteryModelUseCase = defineBatteryModelUseCase;
        this.retrieveAllModelsUseCase = retrieveAllModelsUseCase;
        this.updateBatteryModelUseCase = updateBatteryModelUseCase;
        this.addNewBatteryUseCase = addNewBatteryUseCase;
    }

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
                .build();
        var newBattery = addNewBatteryUseCase.execute(command);
        var response = ResponseMapper.mapToBatteryResponse(newBattery);
        return ResponseEntity.ok(new ApiResponse<>("New battery added successfully", response));
    }
}
