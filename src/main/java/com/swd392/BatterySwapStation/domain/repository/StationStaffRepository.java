package com.swd392.BatterySwapStation.domain.repository;

import com.swd392.BatterySwapStation.domain.entity.StationStaff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StationStaffRepository extends JpaRepository<StationStaff, Long> {
    Optional<StationStaff> findByStaff_Id(UUID userId);

    boolean existsByStaff_Id(UUID userId);

    List<StationStaff> findByStation_Id(UUID stationId);
}
