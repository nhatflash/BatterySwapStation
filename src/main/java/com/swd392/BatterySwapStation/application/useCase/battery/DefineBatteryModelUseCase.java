package com.swd392.BatterySwapStation.application.useCase.battery;

import com.swd392.BatterySwapStation.application.model.command.DefineBatteryModelCommand;
import com.swd392.BatterySwapStation.infrastructure.service.business.BatteryService;
import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.domain.entity.BatteryModel;
import com.swd392.BatterySwapStation.domain.valueObject.BatteryType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DefineBatteryModelUseCase implements IUseCase<DefineBatteryModelCommand, BatteryModel> {

    private final BatteryService batteryService;

    public DefineBatteryModelUseCase(BatteryService batteryService) {
        this.batteryService = batteryService;
    }

    @Override
    @Transactional
    public BatteryModel execute(DefineBatteryModelCommand request) {
        if (batteryService.existsByBatteryType(request.getType())) {
            throw new IllegalArgumentException("Battery type already exists");
        }
        return defineNewModel(request);
    }


    private BatteryModel defineNewModel(DefineBatteryModelCommand request) {
        BatteryModel newModel = BatteryModel.builder()
                .type(new BatteryType(request.getType()))
                .manufacturer(request.getManufacturer())
                .chemistry(request.getChemistry())
                .weightKg(request.getWeightKg())
                .warrantyMonths(request.getWarrantyMonths())
                .maxChargePowerKwh(request.getMaxChargePowerKwh())
                .minSohThreshold(request.getMinSohThreshold())
                .compatibleVehicles(request.getCompatibleVehicles())
                .build();
        return batteryService.saveBatteryModel(newModel);
    }
}
