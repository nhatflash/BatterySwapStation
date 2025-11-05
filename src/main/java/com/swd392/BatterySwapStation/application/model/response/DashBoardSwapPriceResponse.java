package com.swd392.BatterySwapStation.application.model.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DashBoardSwapPriceResponse {
    private String period;                // e.g., DAY, MONTH, YEAR
    private long totalTransactions;        // Tổng số giao dịch
    private long completedTransactions;    // Giao dịch hoàn tất
    private double totalRevenue;          // Tổng doanh thu
    private List<DashBoardDetailResponse> details; //  Thêm vào
}