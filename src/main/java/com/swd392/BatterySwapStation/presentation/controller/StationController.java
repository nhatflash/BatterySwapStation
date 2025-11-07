package com.swd392.BatterySwapStation.presentation.controller;


import com.swd392.BatterySwapStation.application.common.response.ApiResponse;
import com.swd392.BatterySwapStation.application.model.command.ChangeStatusStationCommand;
import com.swd392.BatterySwapStation.application.model.command.CreateStationCommand;
import com.swd392.BatterySwapStation.application.model.command.UpdateStationCommand;
import com.swd392.BatterySwapStation.application.useCase.station.*;
import com.swd392.BatterySwapStation.domain.entity.Station;
import com.swd392.BatterySwapStation.domain.enums.StationStatus;
import com.swd392.BatterySwapStation.presentation.dto.request.CreateStationRequest;
import com.swd392.BatterySwapStation.presentation.dto.request.UpdateStationRequest;
import com.swd392.BatterySwapStation.application.model.response.ChangeStationStatusResponse;
import com.swd392.BatterySwapStation.application.model.response.CreateStationResponse;
import com.swd392.BatterySwapStation.application.model.response.UpdateStationResponse;
import com.swd392.BatterySwapStation.application.common.mapper.ResponseMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/station")
@SecurityRequirement(name = "bearerAuth")
public class StationController {

    @Autowired
    CreateStationUseCase createStationUseCase;

    @Autowired
    ChangeStatusStationUseCase changeStatusStationUseCase;

    @Autowired
    UpdateStationUseCase updateStationUseCase;

    @Autowired
    ListStationAdminUseCase listStationAdminUseCase;

    @Autowired
    ListStationHomePageUseCase listStationHomePage;

    @Autowired
    GetStationDetailUseCase getStationDetailUseCase;


    // ==================== CREATE STATION ==================================
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CreateStationResponse>> createStation(@Valid @RequestBody CreateStationRequest request) {
        var command = CreateStationCommand.builder()
                .name(request.getName())
                .address(request.getAddress())
                .totalCapacity(request.getTotalCapacity())
                .totalSwapBays(request.getTotalSwapBays())
                .status(StationStatus.OPERATIONAL)
                .openingTime(request.getOpeningTime())
                .closingTime(request.getClosingTime())
                .contactPhone(request.getContactPhone())
                .contactEmail(request.getContactEmail())
                .description(request.getDescription())
                .imageUrl(request.getImageUrl())
                .build();

        var newStation = createStationUseCase.execute(command);
        var response = ResponseMapper.mapToCreateResponse(newStation);
        return ResponseEntity.ok(new ApiResponse<>("Create station successfully.", response));
    }

    // ==================== CHANGE STATUS STATION ==================================
    @PatchMapping("{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ChangeStationStatusResponse>> changeStatus(@PathVariable("id") String id,
                                                                                 @RequestParam("status") StationStatus status) {
        var command = ChangeStatusStationCommand.builder()
                .stationId(id)
                .newStatus(status)
                .build();
        var changeStatus = changeStatusStationUseCase.execute(command);
        var response = ResponseMapper.mapToChangeStationStatusResponse(changeStatus);
        return ResponseEntity.ok(new ApiResponse<>("Change status station successfully.", response));
    }

    //====================Update Station==================================
    @PutMapping("/{id}/update")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update station details", description = "Update the details of a station by its ID")
    public ResponseEntity<ApiResponse<UpdateStationResponse>> updateStation(
            @PathVariable("id") UUID id,
            @Valid @RequestBody UpdateStationRequest request) {
        var command = UpdateStationCommand.builder()
                .stationId(id)
                .name(request.getName())
                .address(request.getAddress())
                .totalCapacity(request.getTotalCapacity())
                .totalSwapBays(request.getTotalSwapBays())
                .status(request.getStatus())
                .openingTime(request.getOpeningTime())
                .closingTime(request.getClosingTime())
                .contactPhone(request.getContactPhone())
                .contactEmail(request.getContactEmail())
                .description(request.getDescription())
                .imageUrl(request.getImageUrl())
                .build();

        var updatedStation = updateStationUseCase.execute(command);
        var response = ResponseMapper.mapToUpdateStationResponse(updatedStation);
        return ResponseEntity.ok(new ApiResponse<>("Update station successfully.", response));
    }

    //============================== Get All Stations ==============================
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all stations", description = "Retrieve a list of all stations in the system")
    public ResponseEntity<ApiResponse<List<Station>>> getOperationalStations() {
        var stations = listStationAdminUseCase.execute(null);
        return ResponseEntity.ok(new ApiResponse<>("Get operational stations successfully", stations));
    }

    //============================ Get Station Only Operational ============================
    @GetMapping("/operational")
//    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all operational stations", description = "Retrieve a list of all operational stations in the system")
    public ResponseEntity<ApiResponse<List<Station>>> getAllStations() {
        var stations = listStationHomePage.execute(null);
        return ResponseEntity.ok(new ApiResponse<>("Get all stations successfully", stations));
    }

    //============================ Get Station Detail ============================
    @GetMapping("/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get station by ID", description = "Retrieve a station's details by its ID")
    public ResponseEntity<ApiResponse<Station>> getStationById(@PathVariable("id") UUID id) {
        var station = getStationDetailUseCase.execute(id);
        return ResponseEntity.ok(new ApiResponse<>("Get station by ID successfully", station));
    }
}