package com.swd392.BatterySwapStation.domain.repository;

import com.swd392.BatterySwapStation.domain.entity.Payment;
import com.swd392.BatterySwapStation.domain.entity.SwapTransaction;
import com.swd392.BatterySwapStation.domain.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    List<Payment> findBySwapTransactionOrderByIdDesc(SwapTransaction transaction);

    List<Payment> findByStatusAndSwapTransaction(PaymentStatus status, SwapTransaction swapTransaction);

    List<Payment> findByPaymentDateBetween(LocalDateTime start, LocalDateTime end);
}
