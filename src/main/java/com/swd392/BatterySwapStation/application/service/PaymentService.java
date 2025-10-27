package com.swd392.BatterySwapStation.application.service;


import com.swd392.BatterySwapStation.domain.entity.Payment;
import com.swd392.BatterySwapStation.domain.entity.SwapTransaction;
import com.swd392.BatterySwapStation.domain.enums.PaymentMethod;
import com.swd392.BatterySwapStation.domain.enums.PaymentStatus;
import com.swd392.BatterySwapStation.domain.exception.NotFoundException;
import com.swd392.BatterySwapStation.infrastructure.repository.PaymentRepository;
import com.swd392.BatterySwapStation.infrastructure.repository.SwapTransactionRepository;
import com.swd392.BatterySwapStation.infrastructure.service.VnPayService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final SwapTransactionRepository swapTransactionRepository;
    private final VnPayService vnPayService;

    public PaymentService(PaymentRepository paymentRepository,
                          SwapTransactionRepository swapTransactionRepository,
                          VnPayService vnPayService) {
        this.paymentRepository = paymentRepository;
        this.swapTransactionRepository = swapTransactionRepository;
        this.vnPayService = vnPayService;
    }

    public String processCashPayment(UUID transactionId) {
        var transaction = getValidSwapTransaction(transactionId);
        var payment = Payment.builder()
                .swapTransaction(transaction)
                .amount(transaction.getSwapPrice())
                .method(PaymentMethod.CASH)
                .status(PaymentStatus.COMPLETED)
                .notes("Payment with cash.")
                .build();
        var savedPayment = paymentRepository.save(payment);
        return savedPayment.getId().toString();
    }

    public String generateVnPayUrl(UUID transactionId, HttpServletRequest request) {
        var transaction = getValidSwapTransaction(transactionId);
        Map<String, String> params = vnPayService.getVnpParams(request, transaction);
        return vnPayService.getQueryUrl(params);
    }

    public SwapTransaction getValidSwapTransaction(UUID transactionId) {
        return swapTransactionRepository.findById(transactionId)
                .orElseThrow(() -> new NotFoundException("Transaction not found."));
    }

    public Payment savePayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    public List<Payment> findAllWithOrderDescByTransactionId(SwapTransaction transaction) {
        return paymentRepository.findBySwapTransactionOrderByIdDesc(transaction);
    }

}
