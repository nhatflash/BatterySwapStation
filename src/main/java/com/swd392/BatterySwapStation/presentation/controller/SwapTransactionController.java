package com.swd392.BatterySwapStation.presentation.controller;

import com.swd392.BatterySwapStation.application.common.response.ApiResponse;
import com.swd392.BatterySwapStation.application.model.CreateScheduledBatterySwapCommand;
import com.swd392.BatterySwapStation.infrastructure.security.user.CustomUserDetails;
import com.swd392.BatterySwapStation.presentation.dto.request.CreateScheduledBatterySwapRequest;
import com.swd392.BatterySwapStation.presentation.dto.response.SwapTransactionResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/swap")
public class SwapTransactionController {

    public SwapTransactionController() {}

    public ResponseEntity<ApiResponse<SwapTransactionResponse>> createScheduledSwap(@Valid @RequestBody CreateScheduledBatterySwapRequest request,
                                                                                    @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            throw new UsernameNotFoundException("User not found");
        }
        var command = CreateScheduledBatterySwapCommand.builder()
                .driverId(userDetails.getUserId())
                .vehicleId(request.getVehicleId())
                .swappedBatteries(request.getSwappedBatteries())
                .scheduledTime(request.getScheduledTime())
                .build();

        return null;
    }
}
