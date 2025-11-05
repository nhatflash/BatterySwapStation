package com.swd392.BatterySwapStation.application.useCase.station;


import com.swd392.BatterySwapStation.application.model.command.ChangeStatusStationCommand;
import com.swd392.BatterySwapStation.infrastructure.service.business.StationService;
import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.domain.entity.Station;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChangeStatusStationUseCase implements IUseCase<ChangeStatusStationCommand, Station> {

    @Autowired
    StationService stationService;


    @Override
    public Station execute(ChangeStatusStationCommand request) {
        var station = stationService.getByStationID(UUID.fromString(request.getStationId()));

        station.setStatus(request.getNewStatus());

        return stationService.saveStation(station);
    }
}
