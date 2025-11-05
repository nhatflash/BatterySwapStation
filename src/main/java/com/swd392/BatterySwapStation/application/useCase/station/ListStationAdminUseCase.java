package com.swd392.BatterySwapStation.application.useCase.station;


import com.swd392.BatterySwapStation.infrastructure.service.business.StationService;
import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.domain.entity.Station;
import com.swd392.BatterySwapStation.domain.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListStationAdminUseCase implements IUseCase<Void, List<Station>> {

    private final StationService stationService;

    public ListStationAdminUseCase(StationService stationService) {
        this.stationService = stationService;
    }

    @Override
    public List<Station> execute(Void request) {
        List<Station> stations = stationService.getAllStations();
        if (stations == null || stations.isEmpty()) {
            throw  new NotFoundException("No stations found.");
        }
        return stations;
    }
}
