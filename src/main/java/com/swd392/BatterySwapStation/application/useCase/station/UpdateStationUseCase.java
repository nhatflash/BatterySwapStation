package com.swd392.BatterySwapStation.application.useCase.station;

import com.swd392.BatterySwapStation.application.common.mapper.DateStringMapper;
import com.swd392.BatterySwapStation.application.model.UpdateStationCommand;
import com.swd392.BatterySwapStation.application.service.StationService;
import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.domain.entity.Station;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.UUID;

@Service
public class UpdateStationUseCase implements IUseCase<UpdateStationCommand, Station> {

    @Autowired
    StationService stationService;

    @Override
    public Station execute(UpdateStationCommand request) {
        var station = stationService.getByStationID(request.getStationId());

        // ✅ Convert time strings safely (avoid repeated parsing)
        LocalTime openingTime = DateStringMapper.getLocalTime(request.getOpeningTime());
        LocalTime closingTime = DateStringMapper.getLocalTime(request.getClosingTime());

        if (station != null) {
            // Update station info here
            station.setName(!request.getName().isEmpty() ? request.getName() : station.getName());
            station.setAddress(!request.getAddress().isEmpty() ? request.getAddress() : station.getAddress());
            station.setTotalCapacity(request.getTotalCapacity() != null ? request.getTotalCapacity() : station.getTotalCapacity());
            station.setTotalSwapBays(request.getTotalSwapBays() != null ? request.getTotalSwapBays() : station.getTotalSwapBays());
            station.setOpeningTime(openingTime != null ? openingTime : station.getOpeningTime());
            station.setClosingTime(closingTime != null ? closingTime : station.getClosingTime());
            station.setContactPhone(!request.getContactPhone().isEmpty() ? request.getContactPhone() : station.getContactPhone());
            station.setContactEmail(!request.getContactEmail().isEmpty() ? request.getContactEmail() : station.getContactEmail());
            station.setDescription(!request.getDescription().isEmpty() ? request.getDescription() : station.getDescription());
            station.setImageUrl(!request.getImageUrl().isEmpty() ? request.getImageUrl() : station.getImageUrl());

            return stationService.saveStation(station);
        } else {
            return null;
        }
    }
}
