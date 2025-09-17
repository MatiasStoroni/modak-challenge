package com.challenge.notifications.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Defines a rate limit for a specific notification type,
 * including the maximum allowed notifications and the time window.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RateLimitRule {
    private String notificationType;
    private int maxNotifications;
    private TimeWindow timeWindow;
}
