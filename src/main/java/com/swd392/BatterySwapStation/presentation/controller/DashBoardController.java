package com.swd392.BatterySwapStation.presentation.controller;


import com.swd392.BatterySwapStation.application.common.response.ApiResponse;
import com.swd392.BatterySwapStation.application.model.command.DashBoardCommand;
import com.swd392.BatterySwapStation.application.useCase.dashBoard.DashBoardSwapPriceUseCase;
import com.swd392.BatterySwapStation.domain.entity.Payment;
import com.swd392.BatterySwapStation.domain.enums.DashBoard;
import com.swd392.BatterySwapStation.application.model.response.DashBoardSwapPriceResponse;
import com.swd392.BatterySwapStation.presentation.mapper.ResponseMapper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/station")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class DashBoardController {

    private final DashBoardSwapPriceUseCase dashBoardSwapPriceUseCase;

    @GetMapping("/swap-price")
    public ResponseEntity<ApiResponse<DashBoardSwapPriceResponse>> getDashboard(
            @RequestParam(defaultValue = "DAY") DashBoard type,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime targetDate
    ) {
        DashBoardCommand command = DashBoardCommand.builder()
                .type(type)
                .targetDate(targetDate)
                .build();

        List<Payment> payments = dashBoardSwapPriceUseCase.execute(command);

        //Truyền thẳng List<Payment> vào ResponseMapper
        var response = ResponseMapper.mapToDashBoardSwapPriceResponse(
                command.getType().name(),
                payments
        );

        return ResponseEntity.ok(new ApiResponse<>("Total dashboard", response));
    }
}
