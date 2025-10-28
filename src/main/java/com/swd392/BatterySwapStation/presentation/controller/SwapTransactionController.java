package com.swd392.BatterySwapStation.presentation.controller;

import com.swd392.BatterySwapStation.application.common.response.ApiResponse;
import com.swd392.BatterySwapStation.application.model.ConfirmArrivalCommand;
import com.swd392.BatterySwapStation.application.model.ConfirmScheduledSwapCommand;
import com.swd392.BatterySwapStation.application.model.CreateScheduledBatterySwapCommand;
import com.swd392.BatterySwapStation.application.model.CreateWalkInSwapCommand;
import com.swd392.BatterySwapStation.application.useCase.swapTransaction.*;
import com.swd392.BatterySwapStation.infrastructure.security.user.CustomUserDetails;
import com.swd392.BatterySwapStation.presentation.dto.request.ConfirmScheduledSwapRequest;
import com.swd392.BatterySwapStation.presentation.dto.request.CreateScheduledBatterySwapRequest;
import com.swd392.BatterySwapStation.presentation.dto.request.CreateWalkInSwapRequest;
import com.swd392.BatterySwapStation.presentation.dto.response.SwapTransactionResponse;
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
@RequestMapping("/api/swap")
@SecurityRequirement(name = "bearerAuth")
public class SwapTransactionController {

    private final CreateScheduledBatterySwapUseCase createScheduledBatterySwapUseCase;
    private final ConfirmScheduledSwapUseCase confirmScheduledSwapUseCase;
    private final GetAllUnconfirmedSwapsUseCase getAllUnconfirmedSwapsUseCase;
    private final CreateWalkInSwapUseCase createWalkInSwapUseCase;
    private final ConfirmArrivalUseCase confirmArrivalUseCase;

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


    @PostMapping("/scheduled/confirm")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<ApiResponse<SwapTransactionResponse>> confirmScheduledSwap(@Valid @RequestBody ConfirmScheduledSwapRequest request,
                                                                                     @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            throw new UsernameNotFoundException("User not found");
        }
        var command = ConfirmScheduledSwapCommand.builder()
                .transactionId(request.getTransactionId())
                .staffId(userDetails.getUserId())
                .batteryIds(request.getBatteryIds())
                .build();
        var transaction = confirmScheduledSwapUseCase.execute(command);
        var response = ResponseMapper.mapToSwapTransactionResponse(transaction);
        return ResponseEntity.ok(new ApiResponse<>("Confirm scheduled swap successfully.", response));
    }

    @GetMapping("/scheduled/all")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<ApiResponse<List<SwapTransactionResponse>>> getAllUnconfirmedSwaps(@AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            throw new UsernameNotFoundException("User not found");
        }
        var transactions = getAllUnconfirmedSwapsUseCase.execute(userDetails.getUserId());
        var response = transactions.stream().map(ResponseMapper::mapToSwapTransactionResponse).toList();
        return ResponseEntity.ok(new ApiResponse<>("Get all unconfirmed swaps successfully.", response));
    }


    @PostMapping("/walkIn")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<ApiResponse<SwapTransactionResponse>> createWalkInSwap(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                                 @Valid @RequestBody CreateWalkInSwapRequest request) {
        if (userDetails == null) {
            throw new UsernameNotFoundException("User not found");
        }
        var command = CreateWalkInSwapCommand.builder()
                .staffId(userDetails.getUserId())
                .driverId(request.getDriverId())
                .vehicleId(request.getVehicleId())
                .batteryIds(request.getBatteryIds())
                .build();
        var transactions = createWalkInSwapUseCase.execute(command);
        var response = ResponseMapper.mapToSwapTransactionResponse(transactions);
        return ResponseEntity.ok(new ApiResponse<>("Create walk-in swap successfully.", response));
    }

    @PostMapping("/{transactionId}/confirmArrival")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<ApiResponse<SwapTransactionResponse>> confirmArrival(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                               @PathVariable UUID transactionId) {
        if (userDetails == null) {
            throw new UsernameNotFoundException("User not found");
        }
        var command = ConfirmArrivalCommand.builder()
                .transactionId(transactionId)
                .staffId(userDetails.getUserId())
                .build();
        var confirmedTransaction = confirmArrivalUseCase.execute(command);
        var response = ResponseMapper.mapToSwapTransactionResponse(confirmedTransaction);
        return ResponseEntity.ok(new ApiResponse<>("Confirm arrival successfully.", response));
    }
}
