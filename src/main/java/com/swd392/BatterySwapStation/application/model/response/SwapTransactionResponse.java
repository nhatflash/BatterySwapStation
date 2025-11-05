package com.swd392.BatterySwapStation.application.model.response;

import com.swd392.BatterySwapStation.domain.enums.SwapType;
import com.swd392.BatterySwapStation.domain.enums.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SwapTransactionResponse {

    private UUID transactionId;
    private String code;

    private UUID driverId;
    private UUID vehicleId;
    private UUID stationId;
    private List<BatteryTransactionResponse> batteryTransactionResponses;
    private UUID confirmedStaffId;
    private LocalDateTime scheduledTime;
    private LocalDateTime arrivalTime;
    private LocalDateTime swapStartTime;
    private LocalDateTime swapEndTime;

    private TransactionStatus status;
    private SwapType type;
    private BigDecimal swapPrice;
    private String notes;
    private Integer driverRating;
    private String driverFeedback;
}
