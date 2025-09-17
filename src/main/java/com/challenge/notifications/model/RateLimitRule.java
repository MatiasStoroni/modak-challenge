package com.challenge.notifications.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // getters, setters and additional methods
@AllArgsConstructor
@NoArgsConstructor
public class RateLimitRule {
    private String notificationType;
    private int maxNotifications;
    private TimeWindow timeWindow;
}
