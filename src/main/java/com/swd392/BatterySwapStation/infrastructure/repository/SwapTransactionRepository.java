package com.swd392.BatterySwapStation.infrastructure.repository;

import com.swd392.BatterySwapStation.domain.entity.*;
import com.swd392.BatterySwapStation.domain.enums.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface SwapTransactionRepository extends JpaRepository<SwapTransaction, UUID> {
    @Query("SELECT s FROM SwapTransaction s WHERE s.vehicle = ?1 AND s.status = ?2")
    List<SwapTransaction> findAllByVehicle(Vehicle vehicle, TransactionStatus status);

    List<SwapTransaction> findAllByStation(Station station);

    boolean existsSwapTransactionById(UUID transactionId);

    List<SwapTransaction> findAllByVehicle(Vehicle vehicle);

    int countByStationAndSwapEndTimeIsNotNull(Station station);

    List<SwapTransaction> findByStationAndSwapEndTimeIsNotNull(Station station);

    List<SwapTransaction> findByDriverAndStatus(User driver, TransactionStatus status);

    List<SwapTransaction> findBySwapStartTimeBetween(LocalDateTime start, LocalDateTime end);
}
