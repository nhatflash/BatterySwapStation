package com.swd392.BatterySwapStation.presentation.controller;

import com.swd392.BatterySwapStation.application.common.response.ApiResponse;
import com.swd392.BatterySwapStation.application.model.DefineBatteryModelCommand;
import com.swd392.BatterySwapStation.application.service.BatteryService;
import com.swd392.BatterySwapStation.application.useCase.battery.DefineBatteryModelUseCase;
import com.swd392.BatterySwapStation.presentation.dto.request.DefineBatteryModelRequest;
import com.swd392.BatterySwapStation.presentation.dto.response.BatteryModelResponse;
import com.swd392.BatterySwapStation.presentation.mapper.ResponseMapper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/battery")
@SecurityRequirement(name = "bearerAuth")
public class BatteryController {

    private final DefineBatteryModelUseCase defineBatteryModelUseCase;

    public BatteryController(DefineBatteryModelUseCase defineBatteryModelUseCase) {
        this.defineBatteryModelUseCase = defineBatteryModelUseCase;
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
}
