package com.swd392.BatterySwapStation.application.useCase.battery;

import com.swd392.BatterySwapStation.application.common.mapper.DateStringMapper;
import com.swd392.BatterySwapStation.application.model.AddNewBatteryCommand;
import com.swd392.BatterySwapStation.application.service.BatteryService;
import com.swd392.BatterySwapStation.application.service.StationService;
import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.domain.entity.Battery;
import com.swd392.BatterySwapStation.domain.entity.BatteryModel;
import com.swd392.BatterySwapStation.domain.entity.Station;
import com.swd392.BatterySwapStation.domain.enums.BatteryStatus;
import com.swd392.BatterySwapStation.domain.valueObject.Money;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class AddNewBatteryUseCase implements IUseCase<AddNewBatteryCommand, Battery> {

    private final StationService stationService;
    private final BatteryService batteryService;

    public AddNewBatteryUseCase(StationService stationService, BatteryService batteryService) {
        this.stationService = stationService;
        this.batteryService = batteryService;
    }

    @Override
    public Battery execute(AddNewBatteryCommand request) {
        Station currentStation = getValidStationForBatteryAdding(request.getCurrentStationId());
        BatteryModel model = batteryService.findByBatteryType(request.getType());

        return addNewBattery(currentStation, model, request);
    }

    private Station getValidStationForBatteryAdding(UUID stationId) {
        Station station = stationService.getByStationID(stationId);
        // later will be added like capacity checking
        return station;
    }



    private Battery addNewBattery(Station station, BatteryModel model, AddNewBatteryCommand request) {
        Battery newBattery = Battery.builder()
                .serialNumber(request.getSerialNumber())
                .model(model)
                .capacityKwh(request.getCapacityKwh())
                .status(BatteryStatus.FULL)
                .currentStation(station)
                .currentChargePercentage(BigDecimal.valueOf(100.00))
                .totalChargeCycles(0)
                .totalSwapCount(0)
                .manufactureDate(DateStringMapper.getLocalDate(request.getManufactureDate()))
                .warrantyExpiryDate(DateStringMapper.getLocalDate(request.getWarrantyExpiryDate()))
                .notes(request.getNotes())
                .rentalPrice(new Money(request.getRentalPrice()))
                .build();
        return batteryService.saveBattery(newBattery);
    }
}
