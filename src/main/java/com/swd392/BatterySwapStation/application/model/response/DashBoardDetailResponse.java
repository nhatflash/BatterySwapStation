package com.swd392.BatterySwapStation.application.model.response;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashBoardDetailResponse {
    private String label;      // Ví dụ: "2025-11-01" hoặc "Tháng 11" hoặc "2025"
    private long transactions; // Tổng số giao dịch
    private double revenue;    // Tổng doanh thu
}
