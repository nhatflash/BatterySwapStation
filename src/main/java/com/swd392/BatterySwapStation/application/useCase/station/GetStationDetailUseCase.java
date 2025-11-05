package com.swd392.BatterySwapStation.application.useCase.station;

import com.swd392.BatterySwapStation.infrastructure.service.business.StationService;
import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.domain.entity.Station;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GetStationDetailUseCase implements IUseCase<UUID, Station> {

    @Autowired
    StationService stationService;

    @Override
    public Station execute(UUID id) {
        return stationService.getByStationID(id);
    }
}
