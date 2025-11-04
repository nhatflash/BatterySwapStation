package com.swd392.BatterySwapStation.domain.repository;

import com.swd392.BatterySwapStation.domain.entity.Battery;
import com.swd392.BatterySwapStation.domain.entity.BatteryTransaction;
import com.swd392.BatterySwapStation.domain.entity.SwapTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BatteryTransactionRepository extends JpaRepository<BatteryTransaction, Long> {

    List<BatteryTransaction> findAllBySwapTransaction(SwapTransaction swapTransaction);


    List<BatteryTransaction> findByNewBatteryOrderByIdDesc(Battery newBattery);

    List<BatteryTransaction> findByOldBatteryOrderByIdDesc(Battery oldBattery);

}
