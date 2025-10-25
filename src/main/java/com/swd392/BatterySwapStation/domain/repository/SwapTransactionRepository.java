package com.swd392.BatterySwapStation.domain.repository;

import com.swd392.BatterySwapStation.domain.entity.Battery;
import com.swd392.BatterySwapStation.domain.entity.Station;
import com.swd392.BatterySwapStation.domain.entity.SwapTransaction;
import com.swd392.BatterySwapStation.domain.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface SwapTransactionRepository extends JpaRepository<SwapTransaction, UUID> {
    @Query("SELECT s FROM SwapTransaction s WHERE s.vehicle = ?1 AND s.status = 'COMPLETED' ORDER BY s.id DESC")
    List<SwapTransaction> findAllByVehicleOrderByIdDesc(Vehicle vehicle);

    List<SwapTransaction> findAllByStation(Station station);
}
