package com.swd392.BatterySwapStation.application.service;


import com.swd392.BatterySwapStation.domain.entity.Station;
import com.swd392.BatterySwapStation.domain.entity.User;
import com.swd392.BatterySwapStation.domain.enums.StationStatus;
import com.swd392.BatterySwapStation.domain.exception.NotFoundException;
import com.swd392.BatterySwapStation.domain.repository.StationRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class StationService {


    private final StationRepository stationRepository;


    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

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

}
