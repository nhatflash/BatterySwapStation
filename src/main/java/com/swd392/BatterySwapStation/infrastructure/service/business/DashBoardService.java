package com.swd392.BatterySwapStation.infrastructure.service.business;


import com.swd392.BatterySwapStation.application.service.business.IDashboardService;
import com.swd392.BatterySwapStation.domain.entity.Payment;
import com.swd392.BatterySwapStation.domain.entity.SwapTransaction;
import com.swd392.BatterySwapStation.domain.repository.PaymentRepository;
import com.swd392.BatterySwapStation.domain.repository.SwapTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashBoardService implements IDashboardService {
    private final SwapTransactionRepository swapTransactionRepository;

    private final PaymentRepository paymentRepository;

    public List<Payment> getPaymentsBetween(LocalDateTime start, LocalDateTime end) {
        return paymentRepository.findByPaymentDateBetween(start, end);
    }

    public List<SwapTransaction> getTransactionsBetween(LocalDateTime start, LocalDateTime end) {
        return swapTransactionRepository.findBySwapStartTimeBetween(start, end);
    }
}
