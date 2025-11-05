package com.swd392.BatterySwapStation.application.service.business;

import com.swd392.BatterySwapStation.domain.entity.Payment;
import com.swd392.BatterySwapStation.domain.entity.SwapTransaction;

import java.time.LocalDateTime;
import java.util.List;

public interface IDashboardService {
    List<Payment> getPaymentsBetween(LocalDateTime start, LocalDateTime end);
    List<SwapTransaction> getTransactionsBetween(LocalDateTime start, LocalDateTime end);

}
