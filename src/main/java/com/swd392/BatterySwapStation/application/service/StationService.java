package com.swd392.BatterySwapStation.application.service;


import com.swd392.BatterySwapStation.domain.entity.Station;
import com.swd392.BatterySwapStation.domain.entity.User;
import com.swd392.BatterySwapStation.domain.enums.StationStatus;
import com.swd392.BatterySwapStation.domain.exception.NotFoundException;
import com.swd392.BatterySwapStation.domain.repository.StationRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StationService {


    private final StationRepository stationRepository;


    public Station getByStationID (UUID StationId){
        return stationRepository.findById(StationId)
                .orElseThrow(() -> new NotFoundException("Station not found with ID !!!"));
    }

    public Station saveStation(Station station) {
        return stationRepository.save(station);
    }

    //  Get all stations with status OPERATIONAL
    public List<Station> getOperationalStations() {
        return stationRepository.findByStatus(StationStatus.OPERATIONAL);
    }

    // Get all stations
    public List<Station> getAllStations() {
        return stationRepository.findAll();
    }
    // Get StationByName
    public Station getStationByName(String name) {
        return stationRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException("Station not found with name: " + name));
    }

    public boolean existsByName(String name) {
        return stationRepository.existsByName(name);
    }

    public boolean isStationOperational(Station station) {
        return station.getStatus().equals(StationStatus.OPERATIONAL);
    }

}
