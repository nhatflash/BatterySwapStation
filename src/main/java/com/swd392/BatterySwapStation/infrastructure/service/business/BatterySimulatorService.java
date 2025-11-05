package com.swd392.BatterySwapStation.infrastructure.service.business;

import com.swd392.BatterySwapStation.application.service.business.IBatterySimulatorService;
import com.swd392.BatterySwapStation.domain.entity.Battery;
import com.swd392.BatterySwapStation.domain.enums.BatteryStatus;
import com.swd392.BatterySwapStation.domain.model.BatteryState;
import com.swd392.BatterySwapStation.domain.valueObject.SoH;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;

@Service
public class BatterySimulatorService implements IBatterySimulatorService {

    private final Random random = new Random();

    public BatteryState simulateBatteryState(Battery battery) {
        BatteryState state = BatteryState.builder()
                .batteryId(battery.getId())
                .serialNumber(battery.getSerialNumber())
                .batteryType(battery.getModel().getType().getValue())
                .stateOfHealth(battery.getStateOfHealth().getPercentage().doubleValue())
                .currentStationId(battery.getCurrentStation().getId())
                .abnormal(false)
                .alertLevel("INFO")
                .build();

        switch (battery.getStatus()) {
            case BatteryStatus.CHARGING:
                simulateCharging(state);
                break;
            case BatteryStatus.FULL:
                simulateFull(state);
                break;
            case BatteryStatus.IN_USE:
                simulateInUse(state);
                break;
            case BatteryStatus.MAINTENANCE:
                simulateMaintenance(state);
                break;
            case BatteryStatus.FAULTY:
                simulateFaulty(state);
                break;
            default:
                throw new IllegalArgumentException("Invalid battery status.");

        }
        checkAbnormalities(state);
        return state;
    }


    private void simulateCharging(BatteryState state) {
        state.setStatus(BatteryStatus.CHARGING);

        int baseCharge = 30 + random.nextInt(66);
        state.setChargeLevel(baseCharge);

        double voltage = 350 + (baseCharge * 0.7);
        state.setVoltage(roundTo2Decimals(voltage));

        double current = 100 - (baseCharge * 0.8);
        state.setCurrent(roundTo2Decimals(current));

        double power = voltage * current / 1000;
        state.setPowerKwh(roundTo2Decimals(power));

        double temp = 28 + (baseCharge * 0.12) + randomVariation(3);
        state.setTemperature(roundTo2Decimals(temp));

        int minutesToFull = (int) ((100 - baseCharge) * 1.2);
        state.setEstimatedMinutesToFull(minutesToFull);

        state.setChargingStartedAt(LocalDateTime.now().minusMinutes(baseCharge * 2));
    }

    private void simulateFull(BatteryState state) {
        state.setStatus(BatteryStatus.FULL);
        state.setChargeLevel(100);
        state.setVoltage(roundTo2Decimals(410 + randomVariation(2)));
        state.setCurrent(0.0);
        state.setPowerKwh(0.0);
        state.setTemperature(roundTo2Decimals(23 + randomVariation(2)));
        state.setEstimatedMinutesToFull(0);
    }


    private void simulateInUse(BatteryState state) {
        state.setStatus(BatteryStatus.IN_USE);

        int chargeLevel = 50 + random.nextInt(45);
        state.setChargeLevel(chargeLevel);

        double voltage = 340 + (chargeLevel * 0.75);
        state.setVoltage(roundTo2Decimals(voltage));

        double current = -(40 + random.nextInt(50));
        state.setCurrent(roundTo2Decimals(current));

        double power = voltage * Math.abs(current) / 1000;
        state.setPowerKwh(roundTo2Decimals(power));

        double temp = 32 + randomVariation(8);
        state.setTemperature(roundTo2Decimals(temp));
    }


    private void simulateMaintenance(BatteryState state) {
        state.setStatus(BatteryStatus.MAINTENANCE);
        state.setChargeLevel(60 + random.nextInt(25));
        state.setVoltage(roundTo2Decimals(365 + randomVariation(10)));
        state.setCurrent(0.0);
        state.setPowerKwh(0.0);
        state.setTemperature(roundTo2Decimals(22 + randomVariation(2)));
        state.setAlertLevel("WARNING");
    }

    private void simulateFaulty(BatteryState state) {
        state.setStatus(BatteryStatus.FAULTY);
        state.setChargeLevel(20 + random.nextInt(25));
        state.setVoltage(roundTo2Decimals(290 + randomVariation(15)));
        state.setCurrent(0.0);
        state.setPowerKwh(0.0);
        state.setTemperature(roundTo2Decimals(40 + randomVariation(10)));
        state.setAbnormal(true);
        state.setAbnormalReason("Battery critically faulty!");
        state.setAlertLevel("CRITICAL");
    }

    private void checkAbnormalities(BatteryState state) {
        if (state.getTemperature() > 55) {
            state.setAbnormal(true);
            state.setAbnormalReason("Critical temperature: " + state.getTemperature() + " C");
            state.setAlertLevel("CRITICAL");
            return;
        }
        if (state.getTemperature() > 48) {
            state.setAbnormal(true);
            state.setAbnormalReason("High temperature: " + state.getTemperature() + " C");
            state.setAlertLevel("WARNING");
            return;
        }
        if (state.getVoltage() < 310 || state.getVoltage() > 420) {
            state.setAbnormal(true);
            state.setAbnormalReason("Voltage out of range: " + state.getVoltage() + " V");
            state.setAlertLevel("CRITICAL");
            return;
        }
        if (state.getStateOfHealth() < 40) {
            state.setAbnormal(true);
            state.setAbnormalReason("Critical SoH: " + state.getStateOfHealth() + " %");
            state.setAbnormalReason("CRITICAL");
            return;
        }
        if (state.getStateOfHealth() < 60) {
            state.setAbnormal(true);
            state.setAbnormalReason("Low SoH: " + state.getStateOfHealth() + " %");
            state.setAbnormalReason("WARNING");
        }
    }


    @Transactional
    public int simulateChargingProgress(int currentCharge) {
        if (currentCharge >= 100) {
            return 100;
        }
        int increment = 2 + random.nextInt(3);
        return Math.min(100, currentCharge + increment);
    }

    @Transactional
    public SoH simulateDegradation(Battery battery) {
        SoH currentSoH = battery.getStateOfHealth();

        double baseDegradation = 0.002;

        if (battery.getTotalChargeCycles() > 2000) {
            baseDegradation += 0.003;
        }
        double variation = random.nextDouble() * 0.001;
        double totalDegradation = baseDegradation + variation;

        BigDecimal degradedValue = new BigDecimal(totalDegradation);
        return currentSoH.degrade(degradedValue);
    }


    private double roundTo2Decimals(double value) {
        return Math.round(value * 100) / 100.0;
    }

    private double randomVariation(double maxVariation) {
        return (random.nextDouble() - 0.5) * 2 * maxVariation;
    }
}

