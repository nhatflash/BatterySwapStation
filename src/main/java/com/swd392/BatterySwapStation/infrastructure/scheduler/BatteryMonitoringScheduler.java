package com.swd392.BatterySwapStation.infrastructure.scheduler;

import com.swd392.BatterySwapStation.domain.entity.Battery;
import com.swd392.BatterySwapStation.domain.enums.BatteryStatus;
import com.swd392.BatterySwapStation.domain.model.BatteryState;
import com.swd392.BatterySwapStation.infrastructure.repository.BatteryRepository;
import com.swd392.BatterySwapStation.domain.valueObject.SoH;
import com.swd392.BatterySwapStation.infrastructure.service.BatterySSEService;
import com.swd392.BatterySwapStation.infrastructure.service.BatterySimulatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class BatteryMonitoringScheduler {

    private static final Logger logger = LoggerFactory.getLogger(BatteryMonitoringScheduler.class);

    private final BatteryRepository  batteryRepository;
    private final BatterySimulatorService batterySimulatorService;
    private final BatterySSEService batterySSEService;

    public BatteryMonitoringScheduler(BatteryRepository batteryRepository,
                                      BatterySimulatorService batterySimulatorService,
                                      BatterySSEService batterySSEService) {
        this.batteryRepository = batteryRepository;
        this.batterySimulatorService = batterySimulatorService;
        this.batterySSEService = batterySSEService;
    }

    private final Map<UUID, Integer> chargingProcess = new ConcurrentHashMap<>();


    @Transactional(readOnly = true)
    @Scheduled(fixedRate = 5000)
    public void updateAndBroadCastBatteryStates() {
        try {
            List<Battery> batteries = batteryRepository.findAllWithDetails();

            Map<UUID, List<Battery>> batteriesByStation = batteries.stream()
                    .collect(Collectors.groupingBy(b -> b.getCurrentStation().getId()));

            batteriesByStation.forEach((stationId, stationBatteries) -> {
                if (batterySSEService.getConnectionsCount(stationId) > 0) {
                    for (Battery battery : batteries) {
                        if (battery.getStatus() == BatteryStatus.CHARGING) {
                            updateChargingProcess(battery);
                        }

                        BatteryState state = batterySimulatorService.simulateBatteryState(battery);
                        batterySSEService.broadcastBatteryUpdate(stationId, state);

                        if (state.isAbnormal()) {
                            batterySSEService.broadCastAlert(
                                    stationId,
                                    state.getAlertLevel(),
                                    state.getAbnormalReason(),
                                    battery.getId()
                            );
                        }
                    }
                }
            });
            logger.debug("Broadcasted states for {} batteries across {} stations", batteries.size(), batteriesByStation.size());
        } catch (Exception e) {
            logger.error("Error while broadcasting battery states", e);
        }
    }


    @Transactional(readOnly = true)
    @Scheduled(fixedRate = 600000)
    public void simulateBatteryDegradation() {
        try {
            List<Battery> batteries = batteryRepository.findAllWithDetails();
            int updatedCount = 0;

            for (Battery battery : batteries) {
                SoH newSoh = batterySimulatorService.simulateDegradation(battery);

                double difference = Math.abs(
                        newSoh.getPercentage().doubleValue() - battery.getStateOfHealth().getPercentage().doubleValue()
                );

                if (difference > 0.1) {
                    battery.setStateOfHealth(newSoh);
                    batteryRepository.save(battery);
                    updatedCount++;

                    logger.debug("Battery {} SoH degraded to {}%", battery.getId(), newSoh.getPercentage());
                }
            }
            if (updatedCount > 0) {
                logger.info("Updated SoH for {} batteries", updatedCount);
            }
        } catch (Exception e) {
            logger.error("Error in degradation simulation", e);
        }
    }

    @Scheduled(fixedRate = 60000)
    public void logConnectionStats() {
        try {
            Map<String, Object> stats = batterySSEService.getConnectionStats();
            logger.info("Battery SSE connection stats: {}", stats);
        } catch (Exception e) {
            logger.error("Error while getting connection stats", e);
        }
    }

    private void updateChargingProcess(Battery battery) {
        UUID batteryId = battery.getId();
        int currentCharge = chargingProcess.getOrDefault(batteryId, 35);

        int newCharge = batterySimulatorService.simulateChargingProgress(currentCharge);
        chargingProcess.put(batteryId, newCharge);

        if (newCharge >= 100 && battery.getStatus() == BatteryStatus.CHARGING) {
            battery.setStatus(BatteryStatus.FULL);
            battery.setTotalChargeCycles(battery.getTotalChargeCycles() + 1);
            battery.setCurrentChargePercentage(BigDecimal.valueOf(100));
            batteryRepository.save(battery);
            chargingProcess.remove(batteryId);

            logger.info("Battery {} fully charged", batteryId);

        }
    }


}
