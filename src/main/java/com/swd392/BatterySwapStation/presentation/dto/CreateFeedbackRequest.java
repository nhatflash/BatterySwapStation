package com.swd392.BatterySwapStation.presentation.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateFeedbackRequest {
    @NotBlank(message = "Feedback message is required.")
    private String feedback;

    @NotNull(message = "Rating is required.")
    @Min(value = 1, message = "Rating cannot be lower than 1.")
    @Max(value = 5, message = "Rating cannot be higher than 5.")
    private Integer rating;
}
