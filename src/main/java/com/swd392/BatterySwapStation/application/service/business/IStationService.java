package com.swd392.BatterySwapStation.application.service.business;

import com.swd392.BatterySwapStation.domain.entity.Station;

import java.util.List;
import java.util.UUID;

public interface IStationService {
    Station getByStationID (UUID StationId);
    List<Station> getOperationalStations();
    Station getStationByName(String name);
    boolean existsByName(String name);
    boolean isStationOperational(Station station);
}
