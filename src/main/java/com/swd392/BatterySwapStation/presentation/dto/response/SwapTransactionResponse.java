package com.swd392.BatterySwapStation.presentation.dto.response;

import com.swd392.BatterySwapStation.domain.enums.SwapType;
import com.swd392.BatterySwapStation.domain.enums.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SwapTransactionResponse {

    private String code;

    private String driverId;
    private String vehicleId;
    private String stationId;
    private List<String> oldBatteryIds;
    private List<String> newBatteryIds;
    private String confirmedStaffId;
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
