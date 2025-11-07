package com.swd392.BatterySwapStation.application.useCase.station;

import com.swd392.BatterySwapStation.application.service.business.IStationService;
import com.swd392.BatterySwapStation.infrastructure.service.business.StationService;
import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.domain.entity.Station;
import com.swd392.BatterySwapStation.domain.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListStationHomePageUseCase implements IUseCase<Void, List<Station>> {

    private final IStationService stationService;

    @Override
    public List<Station> execute(Void request) {
        List<Station> stations = stationService.getOperationalStations();
        if (stations == null || stations.isEmpty()) {
            throw new NotFoundException("No operational stations found.");
        }
        return stations;
    }
}
