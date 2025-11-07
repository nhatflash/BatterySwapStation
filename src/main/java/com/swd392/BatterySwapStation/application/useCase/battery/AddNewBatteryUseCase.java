package com.swd392.BatterySwapStation.application.useCase.battery;

import com.swd392.BatterySwapStation.application.common.mapper.DateStringMapper;
import com.swd392.BatterySwapStation.application.common.mapper.ResponseMapper;
import com.swd392.BatterySwapStation.application.model.command.AddNewBatteryCommand;
import com.swd392.BatterySwapStation.application.model.response.BatteryResponse;
import com.swd392.BatterySwapStation.application.service.business.IBatteryService;
import com.swd392.BatterySwapStation.application.service.business.IStationService;
import com.swd392.BatterySwapStation.infrastructure.service.business.BatteryService;
import com.swd392.BatterySwapStation.infrastructure.service.business.StationService;
import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.domain.entity.Battery;
import com.swd392.BatterySwapStation.domain.entity.BatteryModel;
import com.swd392.BatterySwapStation.domain.entity.Station;
import com.swd392.BatterySwapStation.domain.enums.BatteryStatus;
import com.swd392.BatterySwapStation.domain.valueObject.Money;
import com.swd392.BatterySwapStation.domain.valueObject.SoH;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddNewBatteryUseCase implements IUseCase<AddNewBatteryCommand, BatteryResponse> {

    private final IStationService stationService;
    private final IBatteryService batteryService;


    @Override
    @Transactional
    public BatteryResponse execute(AddNewBatteryCommand request) {
        Station currentStation = getValidStationForBatteryAdding(request.getCurrentStationId());
        BatteryModel model = batteryService.findByBatteryType(request.getType());

        Battery newBattery =  addNewBattery(currentStation, model, request);
        currentStation.setCurrentCapacity(currentStation.getCurrentCapacity() + 1);
        stationService.saveStation(currentStation);
        return ResponseMapper.mapToBatteryResponse(newBattery);
    }

    private Station getValidStationForBatteryAdding(UUID stationId) {
        Station station = stationService.getByStationID(stationId);
        if (station.getCurrentCapacity() >= station.getTotalCapacity()) {
            throw new IllegalArgumentException("Battery capacity exceeded on this station. Cannot add more battery now.");
        }
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
                .stateOfHealth(new SoH(BigDecimal.valueOf(100.00)))
                .manufactureDate(DateStringMapper.getLocalDate(request.getManufactureDate()))
                .warrantyExpiryDate(DateStringMapper.getLocalDate(request.getWarrantyExpiryDate()))
                .notes(request.getNotes())
                .rentalPrice(new Money(request.getRentalPrice()))
                .build();
        return batteryService.saveBattery(newBattery);
    }
}
