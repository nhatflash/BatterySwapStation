package com.swd392.BatterySwapStation.application.service.business;

import com.swd392.BatterySwapStation.domain.entity.Payment;
import com.swd392.BatterySwapStation.domain.entity.SwapTransaction;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.UUID;

public interface IPaymentService {
    String processCashPayment(UUID transactionId);
    String generateVnPayUrl(UUID transactionId, HttpServletRequest request);
    SwapTransaction getValidSwapTransaction(UUID transactionId);
    Payment savePayment(Payment payment);
    List<Payment> findAllWithOrderDescByTransactionId(SwapTransaction transaction);
    List<Payment> findCompletedPaymentsWithTransaction(SwapTransaction transaction);

}
