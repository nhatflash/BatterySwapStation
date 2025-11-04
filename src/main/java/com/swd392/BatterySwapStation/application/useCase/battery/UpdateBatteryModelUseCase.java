package com.swd392.BatterySwapStation.application.useCase.battery;

import com.swd392.BatterySwapStation.application.model.UpdateBatteryModelCommand;
import com.swd392.BatterySwapStation.application.service.BatteryService;
import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.domain.entity.BatteryModel;
import com.swd392.BatterySwapStation.domain.valueObject.BatteryType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UpdateBatteryModelUseCase implements IUseCase<UpdateBatteryModelCommand, BatteryModel> {

    private final BatteryService batteryService;

    public UpdateBatteryModelUseCase(BatteryService batteryService) {
        this.batteryService = batteryService;
    }

    @Override
    @Transactional
    public BatteryModel execute(UpdateBatteryModelCommand request) {
        BatteryModel model = batteryService.findByModelId(request.getModelId());
        checkValidRequest(request, model);
        return batteryService.saveBatteryModel(model);
    }

    private void checkValidRequest(UpdateBatteryModelCommand request, BatteryModel model) {
        if (isRequestNotNullAndNotEmpty(request.getType())) {
            if (isNotValidUpdatedType(request.getType(), model)) {
                throw new IllegalArgumentException("This battery type has already been used.");
            }
            model.setType(new BatteryType(request.getType()));
        }
        if (isRequestNotNullAndNotEmpty(request.getManufacturer())) {
            model.setManufacturer(request.getManufacturer());
        }
        if (isRequestNotNullAndNotEmpty(request.getChemistry())) {
            model.setChemistry(request.getChemistry());
        }
        if (isRequestNotNull(request.getWeightKg())) {
            model.setWeightKg(request.getWeightKg());
        }
        if (isRequestNotNull(request.getWarrantyMonths()))  {
            model.setWarrantyMonths(request.getWarrantyMonths());
        }
        if (isRequestNotNull(request.getMaxChargePowerKwh())) {
            model.setMaxChargePowerKwh(request.getMaxChargePowerKwh());
        }
        model.setMinSohThreshold(request.getMinSohThreshold());
    }

    private boolean isRequestNotNullAndNotEmpty(String request) {
        return request != null && !request.isEmpty();
    }

    private boolean isNotValidUpdatedType(String type, BatteryModel model) {
        return !model.getType().getValue().equals(type) && batteryService.existsByBatteryType(type);
    }

    private boolean isRequestNotNull(Integer request) {
        return request != null && request > -1;
    }

}
