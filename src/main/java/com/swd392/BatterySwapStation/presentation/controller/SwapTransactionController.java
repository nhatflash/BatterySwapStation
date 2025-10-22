package com.swd392.BatterySwapStation.presentation.controller;

import com.swd392.BatterySwapStation.application.common.response.ApiResponse;
import com.swd392.BatterySwapStation.application.model.CreateScheduledBatterySwapCommand;
import com.swd392.BatterySwapStation.application.useCase.swapTransaction.CreateScheduledBatterySwapUseCase;
import com.swd392.BatterySwapStation.infrastructure.security.user.CustomUserDetails;
import com.swd392.BatterySwapStation.presentation.dto.request.CreateScheduledBatterySwapRequest;
import com.swd392.BatterySwapStation.presentation.dto.response.SwapTransactionResponse;
import com.swd392.BatterySwapStation.presentation.mapper.ResponseMapper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/swap")
@SecurityRequirement(name = "bearerAuth")
public class SwapTransactionController {

    private final CreateScheduledBatterySwapUseCase createScheduledBatterySwapUseCase;


    public SwapTransactionController(CreateScheduledBatterySwapUseCase createScheduledBatterySwapUseCase) {
        this.createScheduledBatterySwapUseCase = createScheduledBatterySwapUseCase;
    }

    @PostMapping("/scheduled")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<ApiResponse<SwapTransactionResponse>> createScheduledSwap(@Valid @RequestBody CreateScheduledBatterySwapRequest request,
                                                                                    @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            throw new UsernameNotFoundException("User not found");
        }
        var command = CreateScheduledBatterySwapCommand.builder()
                .driverId(userDetails.getUserId())
                .vehicleId(request.getVehicleId())
                .stationId(request.getStationId())
                .scheduledTime(request.getScheduledTime())
                .notes(request.getNotes())
                .build();
        var transaction = createScheduledBatterySwapUseCase.execute(command);
        var response = ResponseMapper.mapToSwapTransactionResponse(transaction);
        return ResponseEntity.ok(new ApiResponse<>("Create scheduled swap successfully.", response));
    }
}
