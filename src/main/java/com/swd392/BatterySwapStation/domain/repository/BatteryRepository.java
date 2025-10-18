package com.swd392.BatterySwapStation.domain.repository;

import com.swd392.BatterySwapStation.domain.entity.Battery;
import com.swd392.BatterySwapStation.domain.entity.Station;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BatteryRepository extends JpaRepository<Battery, UUID> {

    @Override
    Page<Battery> findAll(Pageable pageable);

    @Override
    List<Battery> findAll();

    List<Battery> findByCurrentStation(Station currentStation);
}
