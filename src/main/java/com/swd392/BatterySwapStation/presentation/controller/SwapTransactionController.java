package com.swd392.BatterySwapStation.presentation.controller;

import com.swd392.BatterySwapStation.application.common.response.ApiResponse;
import com.swd392.BatterySwapStation.application.model.command.*;
import com.swd392.BatterySwapStation.application.useCase.driver.ViewHistorySwapUseCase;
import com.swd392.BatterySwapStation.application.useCase.feedback.CreateFeedbackUseCase;
import com.swd392.BatterySwapStation.application.useCase.swapTransaction.*;
import com.swd392.BatterySwapStation.domain.enums.TransactionStatus;
import com.swd392.BatterySwapStation.presentation.dto.request.ConfirmScheduledSwapRequest;
import com.swd392.BatterySwapStation.presentation.dto.request.CreateFeedbackRequest;
import com.swd392.BatterySwapStation.presentation.dto.request.CreateScheduledBatterySwapRequest;
import com.swd392.BatterySwapStation.presentation.dto.request.CreateWalkInSwapRequest;
import com.swd392.BatterySwapStation.application.model.response.SwapTransactionResponse;
import com.swd392.BatterySwapStation.application.common.mapper.ResponseMapper;
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
@RequestMapping("/api/swap")
@SecurityRequirement(name = "bearerAuth")
public class SwapTransactionController {

    private final CreateScheduledBatterySwapUseCase createScheduledBatterySwapUseCase;
    private final ConfirmScheduledSwapUseCase confirmScheduledSwapUseCase;
    private final GetAllUnconfirmedSwapsUseCase getAllUnconfirmedSwapsUseCase;
    private final CreateWalkInSwapUseCase createWalkInSwapUseCase;
    private final ConfirmArrivalUseCase confirmArrivalUseCase;
    private final ProcessSwappingUseCase processSwappingUseCase;
    private final ViewSwapTransactionDetailsUseCase viewSwapTransactionDetailsUseCase;
    private final CreateFeedbackUseCase createFeedbackUseCase;
    private final ViewHistorySwapUseCase viewHistorySwapUseCase;
    private final ViewUnconfirmedSwapByAdminUseCase viewUnconfirmedSwapByAdminUseCase;

    @PostMapping("/scheduled")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<ApiResponse<SwapTransactionResponse>> createScheduledSwap(@Valid @RequestBody CreateScheduledBatterySwapRequest request) {
        var command = CreateScheduledBatterySwapCommand.builder()
                .vehicleId(request.getVehicleId())
                .stationId(request.getStationId())
                .scheduledTime(request.getScheduledTime())
                .notes(request.getNotes())
                .build();
        var transaction = createScheduledBatterySwapUseCase.execute(command);
        var response = ResponseMapper.mapToSwapTransactionResponse(transaction);
        return ResponseEntity.ok(new ApiResponse<>("Create scheduled swap successfully.", response));
    }


    @PostMapping("/scheduled/{transactionId}/confirm")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<ApiResponse<SwapTransactionResponse>> confirmScheduledSwap(@Valid @RequestBody ConfirmScheduledSwapRequest request,
                                                                                     @PathVariable UUID transactionId) {
        var command = ConfirmScheduledSwapCommand.builder()
                .transactionId(transactionId)
                .batteryIds(request.getBatteryIds())
                .build();
        var transaction = confirmScheduledSwapUseCase.execute(command);
        var response = ResponseMapper.mapToSwapTransactionResponse(transaction);
        return ResponseEntity.ok(new ApiResponse<>("Confirm scheduled swap successfully.", response));
    }

    @GetMapping("/scheduled/all")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<ApiResponse<List<SwapTransactionResponse>>> getAllUnconfirmedSwaps() {
        var transactions = getAllUnconfirmedSwapsUseCase.execute(null);
        var response = transactions.stream().map(ResponseMapper::mapToSwapTransactionResponse).toList();
        return ResponseEntity.ok(new ApiResponse<>("Get all unconfirmed swaps successfully.", response));
    }


    @PostMapping("/walkIn")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<ApiResponse<SwapTransactionResponse>> createWalkInSwap(@Valid @RequestBody CreateWalkInSwapRequest request) {
        var command = CreateWalkInSwapCommand.builder()
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
    public ResponseEntity<ApiResponse<SwapTransactionResponse>> confirmArrival(@PathVariable UUID transactionId) {
        var command = ConfirmArrivalCommand.builder()
                .transactionId(transactionId)
                .build();
        var confirmedTransaction = confirmArrivalUseCase.execute(command);
        var response = ResponseMapper.mapToSwapTransactionResponse(confirmedTransaction);
        return ResponseEntity.ok(new ApiResponse<>("Confirm arrival successfully.", response));
    }

    @PostMapping("/{transactionId}/swapping")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<ApiResponse<SwapTransactionResponse>> processSwapping(@PathVariable UUID transactionId,
                                                                                @RequestParam Boolean isProcessing) {
        if (isProcessing == null) {
            throw new IllegalArgumentException("Processing state is required.");
        }
        var command = ProcessSwappingCommand.builder()
                .transactionId(transactionId)
                .isProcessing(isProcessing)
                .build();
        var processedTransaction = processSwappingUseCase.execute(command);
        var response = ResponseMapper.mapToSwapTransactionResponse(processedTransaction);
        return ResponseEntity.ok(new ApiResponse<>("Process swap successfully.", response));
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<ApiResponse<SwapTransactionResponse>> getSwapTransaction(@PathVariable UUID transactionId) {
        var transaction = viewSwapTransactionDetailsUseCase.execute(transactionId);
        var response = ResponseMapper.mapToSwapTransactionResponse(transaction);
        return ResponseEntity.ok(new ApiResponse<>("Get swap transaction successfully.", response));
    }

    @PostMapping("/{transactionId}/feedback")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<ApiResponse<SwapTransactionResponse>> createFeedbackForSwapTransaction(@PathVariable UUID transactionId,
                                                                                                 @Valid @RequestBody CreateFeedbackRequest request) {
        var command = CreateFeedbackCommand.builder()
                .transactionId(transactionId)
                .feedback(request.getFeedback())
                .rating(request.getRating())
                .build();
        var transaction = createFeedbackUseCase.execute(command);
        var response = ResponseMapper.mapToSwapTransactionResponse(transaction);
        return ResponseEntity.ok(new ApiResponse<>("Create feedback successfully.", response));
    }


    @GetMapping("/history")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<ApiResponse<List<SwapTransactionResponse>>> viewHistorySwap(@RequestParam TransactionStatus status) {
        var command = ViewHistorySwapCommand.builder()
                .status(status)
                .build();
        var transactions = viewHistorySwapUseCase.execute(command);
        var response = transactions.stream().map(ResponseMapper::mapToSwapTransactionResponse).toList();
        return ResponseEntity.ok(new ApiResponse<>("View history swap successfully.", response));
    }

    @GetMapping("/{stationId}/unconfirmed")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<SwapTransactionResponse>>> viewUnconfirmedSwapByAdmin(@PathVariable UUID stationId) {
        var command = ViewUnconfirmedSwapByAdminCommand.builder()
                .stationId(stationId)
                .build();
        var transactions = viewUnconfirmedSwapByAdminUseCase.execute(command);
        var response = transactions.stream().map(ResponseMapper::mapToSwapTransactionResponse).toList();
        return ResponseEntity.ok(new ApiResponse<>("View unconfirmed swap by admin successfully.", response));
    }
}
