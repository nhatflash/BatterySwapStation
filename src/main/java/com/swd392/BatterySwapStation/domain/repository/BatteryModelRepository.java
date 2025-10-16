package com.swd392.BatterySwapStation.domain.repository;

import com.swd392.BatterySwapStation.domain.entity.BatteryModel;
import com.swd392.BatterySwapStation.domain.valueObject.BatteryType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BatteryModelRepository extends JpaRepository<BatteryModel, UUID> {
    boolean existsByType(BatteryType type);
}
