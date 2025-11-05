package com.swd392.BatterySwapStation.application.useCase.payment;

import com.swd392.BatterySwapStation.application.model.command.ProcessPaymentCommand;
import com.swd392.BatterySwapStation.infrastructure.service.business.PaymentService;
import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.domain.enums.PaymentMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProcessPaymentUseCase implements IUseCase<ProcessPaymentCommand, String> {

    private final PaymentService paymentService;

    public ProcessPaymentUseCase(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Override
    @Transactional
    public String execute(ProcessPaymentCommand request) {
        String response = "";
        switch (request.getMethod()) {
            case PaymentMethod.CASH:
                response = paymentService.processCashPayment(request.getTransactionId());
                break;
            case PaymentMethod.VNPAY:
                response = paymentService.generateVnPayUrl(request.getTransactionId(), request.getServletRequest());
                break;
            default:
                break;
        }
        return response;
    }
}
