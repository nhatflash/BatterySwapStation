package com.swd392.BatterySwapStation.application.model.command;


import com.swd392.BatterySwapStation.domain.enums.PaymentMethod;
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
    private PaymentMethod method;
    private HttpServletRequest servletRequest;
}
