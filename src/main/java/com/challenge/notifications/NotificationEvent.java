package com.challenge.notifications;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents a single notification event sent to a user,
 * including its type, recipient, and timestamp.
 */
@Data
@AllArgsConstructor
public class NotificationEvent {
    private String notificationType;
    private String userId;
    private LocalDateTime timestamp;
}
