package com.swd392.BatterySwapStation.application.service;

import com.swd392.BatterySwapStation.domain.entity.BatteryModel;
import com.swd392.BatterySwapStation.domain.repository.BatteryModelRepository;
import com.swd392.BatterySwapStation.domain.valueObject.BatteryType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BatteryService {

    private final BatteryModelRepository batteryModelRepository;

    private static final int LIST_SIZE = 10;

    public BatteryService(BatteryModelRepository batteryModelRepository) {
        this.batteryModelRepository = batteryModelRepository;
    }

    public boolean existsByBatteryType(String batteryType) {
        BatteryType type = new BatteryType(batteryType);
        return batteryModelRepository.existsByType(type);
    }

    public BatteryModel findByModelId(UUID modelId) {
        return batteryModelRepository.findById(modelId)
                .orElseThrow(() -> new IllegalArgumentException("Battery model not found"));
    }

    public List<BatteryModel> findAllModels(int page) {
        if (page < 1) throw new IllegalArgumentException("Request page must be equal or greater than 1.");
        Pageable pageable = PageRequest.of(page, LIST_SIZE);
        return batteryModelRepository.findAll(pageable).getContent();
    }

    public BatteryModel saveBatteryModel(BatteryModel batteryModel) {
        return batteryModelRepository.save(batteryModel);
    }
}
