package com.swd392.BatterySwapStation.presentation.controller;

import com.swd392.BatterySwapStation.application.common.response.ApiResponse;
import com.swd392.BatterySwapStation.application.model.ProcessPaymentCommand;
import com.swd392.BatterySwapStation.application.service.PaymentService;
import com.swd392.BatterySwapStation.application.useCase.payment.ProcessPaymentUseCase;
import com.swd392.BatterySwapStation.domain.enums.PaymentMethod;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final ProcessPaymentUseCase processPaymentUseCase;

    public PaymentController(ProcessPaymentUseCase processPaymentUseCase) {
        this.processPaymentUseCase = processPaymentUseCase;
    }

    @GetMapping("/process")
    public ResponseEntity<ApiResponse<String>> processPayment(@RequestParam UUID transactionId, @RequestParam PaymentMethod method, HttpServletRequest request) {
        var command = ProcessPaymentCommand.builder()
                .transactionId(transactionId)
                .method(method)
                .servletRequest(request)
                .build();
        var response = processPaymentUseCase.execute(command);
        return ResponseEntity.ok(new ApiResponse<>("Payment process successfully", response));
    }
}
