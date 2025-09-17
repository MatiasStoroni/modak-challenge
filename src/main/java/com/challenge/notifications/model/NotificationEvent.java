package com.challenge.notifications.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a single notification event sent to a user,
 * including its type, recipient, and timestamp.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationEvent {
    private String notificationType;
    private String userId;
    private LocalDateTime timestamp;
}
