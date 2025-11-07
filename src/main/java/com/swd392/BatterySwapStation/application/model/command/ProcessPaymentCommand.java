package com.swd392.BatterySwapStation.application.model.command;


import com.swd392.BatterySwapStation.application.enums.PaymentMethodReq;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessPaymentCommand {
    private UUID transactionId;
    private PaymentMethodReq method;
    private HttpServletRequest servletRequest;
}
