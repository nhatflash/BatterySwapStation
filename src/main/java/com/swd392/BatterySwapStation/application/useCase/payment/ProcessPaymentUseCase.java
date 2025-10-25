package com.swd392.BatterySwapStation.application.useCase.payment;

import com.swd392.BatterySwapStation.application.model.ProcessPaymentCommand;
import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.domain.entity.Payment;

import java.util.UUID;

public class ProcessPaymentUseCase implements IUseCase<ProcessPaymentCommand, String> {


    @Override
    public String execute(ProcessPaymentCommand request) {
        return null;
    }
}
