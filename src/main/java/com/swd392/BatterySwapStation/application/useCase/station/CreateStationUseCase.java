package com.swd392.BatterySwapStation.application.useCase.station;

import com.swd392.BatterySwapStation.application.common.mapper.DateStringMapper;
import com.swd392.BatterySwapStation.application.model.CreateStationCommand;
import com.swd392.BatterySwapStation.application.service.StationService;
import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.domain.entity.Station;
import com.swd392.BatterySwapStation.domain.enums.StationStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CreateStationUseCase implements IUseCase<CreateStationCommand, Station> {

    @Autowired
    StationService stationService;

    @Override
    public Station execute(CreateStationCommand request) {


        Station newStation = Station.builder()
                .name(request.getName())
                .idleSwapBays(request.getTotalSwapBays())
                .currentCapacity(0)
                .address(request.getAddress())
                .totalCapacity(request.getTotalCapacity())
                .totalSwapBays(request.getTotalSwapBays())
                .status(StationStatus.OPERATIONAL)
                .openingTime(DateStringMapper.getLocalTime(request.getOpeningTime()))
                .closingTime(DateStringMapper.getLocalTime(request.getClosingTime()))
                .contactPhone(request.getContactPhone())
                .contactEmail(request.getContactEmail())
                .description(request.getDescription())
                .imageUrl(request.getImageUrl())
                .build();
        if (stationService.existsByName(request.getName())) {
            throw new IllegalArgumentException("Station with name " + request.getName() + " already exists.");
        }

        return stationService.saveStation(newStation);
    }
}
