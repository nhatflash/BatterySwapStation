package com.swd392.BatterySwapStation.application.useCase.dashBoard;

import com.swd392.BatterySwapStation.application.model.command.DashBoardCommand;
import com.swd392.BatterySwapStation.application.service.business.IDashboardService;
import com.swd392.BatterySwapStation.infrastructure.service.business.DashBoardService;
import com.swd392.BatterySwapStation.application.useCase.IUseCase;
import com.swd392.BatterySwapStation.domain.entity.Payment;
import com.swd392.BatterySwapStation.domain.enums.DashBoard;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashBoardSwapPriceUseCase implements IUseCase<DashBoardCommand, List<Payment>> {

    private final IDashboardService dashBoardService;

    @Override
    public List<Payment> execute(DashBoardCommand command) {
        DashBoard type = command.getType();
        LocalDateTime target = command.getTargetDate() != null ? command.getTargetDate() : LocalDateTime.now();

        LocalDateTime start;
        LocalDateTime end;

        switch (type) {
            case DAY -> {
                // chỉ hôm nay
                LocalDate today = target.toLocalDate();
                start = today.atStartOfDay();
                end = start.plusDays(1);
            }
            case MONTH -> {
                // toàn bộ 12 tháng của năm hiện tại
                LocalDate firstDayOfYear = target.toLocalDate().withDayOfYear(1);
                start = firstDayOfYear.atStartOfDay();
                end = start.plusYears(1);
            }
            case YEAR -> {
                // từ đầu năm target tới hết năm
                LocalDate firstDayOfYear = target.toLocalDate().with(TemporalAdjusters.firstDayOfYear());
                start = firstDayOfYear.atStartOfDay();
                end = start.plusYears(1);
            }
            default -> throw new IllegalArgumentException("Invalid dashboard type");
        }

        System.out.println("Start: " + start + ", End: " + end);

        return dashBoardService.getPaymentsBetween(start, end);
    }
}