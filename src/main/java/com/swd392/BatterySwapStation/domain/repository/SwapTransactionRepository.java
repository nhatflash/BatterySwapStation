package com.swd392.BatterySwapStation.domain.repository;

import com.swd392.BatterySwapStation.domain.entity.SwapTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SwapTransactionRepository extends JpaRepository<SwapTransaction, UUID> {
}
