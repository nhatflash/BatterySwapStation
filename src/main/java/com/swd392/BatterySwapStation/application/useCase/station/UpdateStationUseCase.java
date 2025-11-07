package com.swd392.BatterySwapStation.application.useCase.station;

import com.swd392.BatterySwapStation.application.common.mapper.DateStringMapper;
import com.swd392.BatterySwapStation.application.model.command.UpdateStationCommand;
import com.swd392.BatterySwapStation.application.service.business.IStationService;
import com.swd392.BatterySwapStation.infrastructure.service.business.StationService;
import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.domain.entity.Station;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Optional;

@Service
public class UpdateStationUseCase implements IUseCase<UpdateStationCommand, Station> {

    @Autowired
    IStationService stationService;

    @Override
    public Station execute(UpdateStationCommand request) {
        var station = stationService.getByStationID(request.getStationId());
        if (station == null) return null;

        LocalTime openingTime = request.getOpeningTime() != null
                ? DateStringMapper.getLocalTime(request.getOpeningTime())
                : null;
        LocalTime closingTime = request.getClosingTime() != null
                ? DateStringMapper.getLocalTime(request.getClosingTime())
                : null;


        station.setName(Optional.ofNullable(request.getName()).filter(s -> !s.isBlank()).orElse(station.getName()));
        station.setAddress(Optional.ofNullable(request.getAddress()).filter(s -> !s.isBlank()).orElse(station.getAddress()));
        station.setTotalCapacity(Optional.ofNullable(request.getTotalCapacity()).orElse(station.getTotalCapacity()));
        station.setTotalSwapBays(Optional.ofNullable(request.getTotalSwapBays()).orElse(station.getTotalSwapBays()));
        station.setOpeningTime(openingTime != null ? openingTime : station.getOpeningTime());
        station.setClosingTime(closingTime != null ? closingTime : station.getClosingTime());
        station.setContactPhone(Optional.ofNullable(request.getContactPhone()).filter(s -> !s.isBlank()).orElse(station.getContactPhone()));
        station.setContactEmail(Optional.ofNullable(request.getContactEmail()).filter(s -> !s.isBlank()).orElse(station.getContactEmail()));
        station.setDescription(Optional.ofNullable(request.getDescription()).filter(s -> !s.isBlank()).orElse(station.getDescription()));
        station.setImageUrl(Optional.ofNullable(request.getImageUrl()).filter(s -> !s.isBlank()).orElse(station.getImageUrl()));
        station.setStatus(Optional.ofNullable(request.getStatus()).orElse(station.getStatus()));

        return stationService.saveStation(station);
    }
}
