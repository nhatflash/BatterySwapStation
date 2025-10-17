package com.swd392.BatterySwapStation.presentation.controller;

import com.swd392.BatterySwapStation.application.common.response.ApiResponse;
import com.swd392.BatterySwapStation.application.model.CreateStationStaffCommand;
import com.swd392.BatterySwapStation.application.model.UpdateStaffDetailCommand;
import com.swd392.BatterySwapStation.application.useCase.stationStaff.*;
import com.swd392.BatterySwapStation.domain.enums.EmploymentStatus;
import com.swd392.BatterySwapStation.domain.valueObject.Money;
import com.swd392.BatterySwapStation.presentation.dto.request.CreateStationStaffRequest;
import com.swd392.BatterySwapStation.presentation.dto.request.UpdateStaffRequest;
import com.swd392.BatterySwapStation.presentation.dto.response.StationStaffResponse;
import com.swd392.BatterySwapStation.presentation.mapper.ResponseMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/station-staff")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class StationStaffController {

    private final CreateStaffUseCase createStaffUseCase;
    private final UpdateStaffStatusUseCase updateStaffStatusUseCase;
    private final ListStaffOfStationUseCase getStationStaffListUseCase;
    private final DeleteStaffUseCase deleteStaffUseCase;
    private final ListAllStaffUseCase getAllStaffUseCase;


    //======================= Add Station Staff =====================//
    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @Operation(summary = "Create Station Staff", description = "Create a new staff member for a battery swap station")
    public ResponseEntity<ApiResponse<StationStaffResponse>> createStationStaff(@Valid @RequestBody CreateStationStaffRequest request,
                                                                                @RequestParam("status") EmploymentStatus status) {
        var command = CreateStationStaffCommand.builder()
                .StaffEmail(request.getStaffEmail())
                .stationName(request.getStationName())
                .salary(request.getSalary())
                .status(status)
                .build();
        // Implementation goes here
        var stationStaff = createStaffUseCase.execute(command);
        var response = ResponseMapper.mapToStationStaffResponse(stationStaff);
        return ResponseEntity.ok(new ApiResponse<>("Create station-staff successfully.", response));
    }

    //======================= Update Station Staff Detail =====================//
    @PutMapping("/update/{staffId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @Operation(summary = "Update Station Staff Detail", description = "Update details of an existing station staff member")
    public ResponseEntity<ApiResponse<StationStaffResponse>> updateStationStaffDetail(
            @PathVariable("staffId") UUID staffId, @Valid @RequestBody UpdateStaffRequest request,
            @RequestParam("status") EmploymentStatus status
    ) {
        var command = UpdateStaffDetailCommand.builder()
                .staffId(staffId)
                .salary(new Money(request.getSalary()))
                .status(status)
                .build();
        var updatedStaff = updateStaffStatusUseCase.execute(command);
        var response = ResponseMapper.mapToStationStaffResponse(updatedStaff);
        return ResponseEntity.ok(new ApiResponse<>("Update station-staff detail successfully.", response));
    }

    // ====================== Get Staffs by Station ID =====================//
    @GetMapping("/station/{stationId}/staff")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @Operation(summary = "Get all staff in a station")
    public ResponseEntity<ApiResponse<List<StationStaffResponse>>> getStaffByStation(
            @PathVariable UUID stationId
    ) {
        var staffList = getStationStaffListUseCase.execute(stationId);
        var responseList = staffList.stream()
                .map(ResponseMapper::mapToStationStaffResponse)
                .toList();

        return ResponseEntity.ok(new ApiResponse<>("Fetched staff list successfully", responseList));
    }

    // ======================== Delete Station Staffs =====================//
    @DeleteMapping("/delete/{staffId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a staff member", description = "Delete a staff member by staffId")
    public ResponseEntity<ApiResponse<Void>> deleteStaff(@PathVariable UUID staffId) {
        deleteStaffUseCase.execute(staffId);
        return ResponseEntity.ok(new ApiResponse<>("Staff deleted successfully.", null));
    }

    //========================== Get All Staffs ==========================//
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @Operation(summary = "Get all staff members", description = "Retrieve a list of all staff members")
    public ResponseEntity<ApiResponse<List<StationStaffResponse>>> getAllStaffs() {
        // Implementation goes here
        var staffList = getAllStaffUseCase.execute(null); // Assuming null fetches all staffs
        var responseList = staffList.stream()
                .map(ResponseMapper::mapToStationStaffResponse)
                .toList();
        return ResponseEntity.ok(new ApiResponse<>("Fetched all staff members successfully.", responseList));
    }
}