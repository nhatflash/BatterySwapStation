package com.swd392.BatterySwapStation.presentation.controller;

import com.swd392.BatterySwapStation.application.common.response.ApiResponse;
import com.swd392.BatterySwapStation.application.model.ProcessPaymentCommand;
import com.swd392.BatterySwapStation.application.service.PaymentService;
import com.swd392.BatterySwapStation.application.useCase.payment.ProcessPaymentUseCase;
import com.swd392.BatterySwapStation.domain.enums.PaymentMethod;
import com.swd392.BatterySwapStation.infrastructure.service.VnPayService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class PaymentController {

    private final ProcessPaymentUseCase processPaymentUseCase;
    private final VnPayService vnPayService;

    public PaymentController(ProcessPaymentUseCase processPaymentUseCase, VnPayService vnPayService) {
        this.processPaymentUseCase = processPaymentUseCase;
        this.vnPayService = vnPayService;
    }

    @GetMapping("/api/payment/process")
    public ResponseEntity<ApiResponse<String>> processPayment(@RequestParam UUID transactionId, @RequestParam PaymentMethod method, HttpServletRequest request) {
        var command = ProcessPaymentCommand.builder()
                .transactionId(transactionId)
                .method(method)
                .servletRequest(request)
                .build();
        var response = processPaymentUseCase.execute(command);
        return ResponseEntity.ok(new ApiResponse<>("Payment process successfully", response));
    }


    @GetMapping("/vnpay-ipn")
    public ResponseEntity<ApiResponse<String>> handleIpnVnPay(HttpServletRequest request) {
        var response = vnPayService.processIPN(request);
        return ResponseEntity.ok(new ApiResponse<>("Payment done processing.", response));
    }


    @GetMapping("/vnpay-return")
    public ResponseEntity<ApiResponse<String>> VnPayRedirect(HttpServletRequest request) {
        String responseCode = request.getParameter("vnp_ResponseCode");
        if ("00".equals(responseCode)) {
            return ResponseEntity.ok(new ApiResponse<>("Payment done processing.", "Payment success."));
        } else {
            return ResponseEntity.ok(new ApiResponse<>("Payment done processing.", "Payment failed."));
        }
    }
}
