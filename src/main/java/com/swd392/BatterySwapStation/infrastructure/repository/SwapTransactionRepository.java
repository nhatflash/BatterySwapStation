package com.swd392.BatterySwapStation.infrastructure.repository;

import com.swd392.BatterySwapStation.domain.entity.Battery;
import com.swd392.BatterySwapStation.domain.entity.Station;
import com.swd392.BatterySwapStation.domain.entity.SwapTransaction;
import com.swd392.BatterySwapStation.domain.entity.Vehicle;
import com.swd392.BatterySwapStation.domain.enums.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface SwapTransactionRepository extends JpaRepository<SwapTransaction, UUID> {
    @Query("SELECT s FROM SwapTransaction s WHERE s.vehicle = ?1 AND s.status = ?2 ORDER BY s.id DESC")
    List<SwapTransaction> findAllByVehicleOrderByIdDesc(Vehicle vehicle, TransactionStatus status);

    List<SwapTransaction> findAllByStation(Station station);

    boolean existsSwapTransactionById(UUID transactionId);

    List<SwapTransaction> findAllByVehicle(Vehicle vehicle);

    int countByStationAndSwapEndTimeIsNotNull(Station station);

    List<SwapTransaction> findByStationAndSwapEndTimeIsNotNull(Station station);
}
