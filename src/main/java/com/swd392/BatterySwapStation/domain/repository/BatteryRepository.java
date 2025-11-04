package com.swd392.BatterySwapStation.domain.repository;

import com.swd392.BatterySwapStation.domain.entity.Battery;
import com.swd392.BatterySwapStation.domain.entity.BatteryModel;
import com.swd392.BatterySwapStation.domain.entity.Station;
import com.swd392.BatterySwapStation.domain.enums.BatteryStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface BatteryRepository extends JpaRepository<Battery, UUID> {

    @Override
    Page<Battery> findAll(Pageable pageable);

    @Override
    List<Battery> findAll();

    List<Battery> findByCurrentStation(Station currentStation);

    List<Battery> findByCurrentStationAndModel(Station currentStation, BatteryModel model);

    int countByCurrentStationAndModel(Station currentStation, BatteryModel model);

    Page<Battery> findByCurrentStationAndStatus(Station currentStation, BatteryStatus status, Pageable pageable);

    @Query("""
        SELECT b FROM Battery b
        JOIN FETCH b.model
        JOIN FETCH b.currentStation
""")
    List<Battery> findAllWithDetails();

    @Query("""
        SELECT b FROM Battery b
        LEFT JOIN FETCH b.model
        LEFT JOIN FETCH b.currentStation
        WHERE b.currentStation.id = :stationId
""")
    List<Battery> findByCurrentStationWithDetails(@Param("stationId") UUID stationId);

}
