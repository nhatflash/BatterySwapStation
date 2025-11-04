package com.swd392.BatterySwapStation.application.service;


import com.swd392.BatterySwapStation.domain.entity.Payment;
import com.swd392.BatterySwapStation.domain.entity.SwapTransaction;
import com.swd392.BatterySwapStation.domain.enums.PaymentMethod;
import com.swd392.BatterySwapStation.domain.enums.PaymentStatus;
import com.swd392.BatterySwapStation.domain.exception.NotFoundException;
import com.swd392.BatterySwapStation.domain.repository.PaymentRepository;
import com.swd392.BatterySwapStation.domain.repository.SwapTransactionRepository;
import com.swd392.BatterySwapStation.infrastructure.service.VnPayService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final SwapTransactionRepository swapTransactionRepository;
    private final VnPayService vnPayService;


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
        SwapTransaction transaction = swapTransactionRepository.findById(transactionId)
                .orElseThrow(() -> new NotFoundException("Transaction not found."));
        List<Payment> completedPayments = findCompletedPaymentsWithTransaction(transaction);
        if (!completedPayments.isEmpty()) {
            throw new IllegalArgumentException("This transaction has been paid successfully. Cannot perform another payment process.");
        }
        return transaction;
    }

    public Payment savePayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    public List<Payment> findAllWithOrderDescByTransactionId(SwapTransaction transaction) {
        return paymentRepository.findBySwapTransactionOrderByIdDesc(transaction);
    }

    public List<Payment> findCompletedPaymentsWithTransaction(SwapTransaction transaction) {
        return paymentRepository.findByStatusAndSwapTransaction(PaymentStatus.COMPLETED, transaction);
    }

}
