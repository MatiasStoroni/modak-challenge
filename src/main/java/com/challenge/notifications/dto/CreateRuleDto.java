package com.challenge.notifications.dto;

import com.challenge.notifications.model.TimeWindow;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateRuleDto {

    @NotBlank(message = "Notification type is required")
    private String notificationType;

    @Min(value = 1, message = "Max notifications must be at least 1")
    @Max(value = 100, message = "Max notifications cannot exceed 100")
    private int maxNotifications;

    @NotNull(message = "Time window is required")
    private TimeWindow timeWindow;
}
