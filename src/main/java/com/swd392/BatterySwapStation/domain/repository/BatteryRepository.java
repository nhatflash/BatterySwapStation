package com.swd392.BatterySwapStation.domain.repository;

import com.swd392.BatterySwapStation.domain.entity.Battery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BatteryRepository extends JpaRepository<Battery, UUID> {
}
