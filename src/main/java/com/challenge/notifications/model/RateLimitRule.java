package com.challenge.notifications.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Defines a rate limit for a specific notification type,
 * including the maximum allowed notifications and the time window.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RateLimitRule {
    private String notificationType;
    private int maxNotifications;
    private TimeWindow timeWindow;
}
