package com.swd392.BatterySwapStation.presentation.controller;

import com.swd392.BatterySwapStation.application.common.response.ApiResponse;
import com.swd392.BatterySwapStation.application.model.command.ProcessPaymentCommand;
import com.swd392.BatterySwapStation.application.useCase.payment.ProcessPaymentUseCase;
import com.swd392.BatterySwapStation.domain.enums.PaymentMethod;
import com.swd392.BatterySwapStation.infrastructure.service.business.VnPayService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

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
        return ResponseEntity.ok(new ApiResponse<>("Payment processes successfully", response));
    }


    @GetMapping("/vnpay-ipn")
    public ResponseEntity<ApiResponse<String>> handleIpnVnPay(HttpServletRequest request) {
        var response = vnPayService.processIPN(request);
        return ResponseEntity.ok(new ApiResponse<>("Payment done processing.", response));
    }


    @GetMapping("/vnpay-return")
    public RedirectView VnPayRedirect(HttpServletRequest request) {
        String responseCode = request.getParameter("vnp_ResponseCode");
        if ("00".equals(responseCode)) {
            return new RedirectView("https://swd-392-topic3-fe.vercel.app/payment/success");
        } else {
            return new RedirectView("https://swd-392-topic3-fe.vercel.app/payment/failure");
        }
    }
}
