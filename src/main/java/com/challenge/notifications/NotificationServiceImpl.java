package com.challenge.notifications;

import java.time.LocalDateTime;
import java.util.List;

import com.challenge.rateLimit.RateLimitRule;
import com.challenge.rateLimit.TimeWindow;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private Gateway gateway;
    private List<RateLimitRule> rules;
    private List<NotificationEvent> notificationsHistory;

    @Override
    public void send(String type, String userId, String message) {

        // Find the rules for this message type (if any)
        List<RateLimitRule> rulesForType = rules.stream()
                .filter(rule -> rule.getNotificationType().equals(type))
                .toList();

        // If there are no rules for this message type -> send notification directly
        if (rulesForType.isEmpty()) {
            gateway.send(userId, message);
            notificationsHistory.add(new NotificationEvent(type, userId, LocalDateTime.now()));
            return;
        }

        // Validate ALL rules
        for (RateLimitRule rule : rulesForType) {
            LocalDateTime windowStart = calculateWindowStart(rule.getTimeWindow());
            int windowMessageCount = countMessagesInWindow(windowStart, userId, type);

            // If ANY rule fails, reject the message immediately
            if (windowMessageCount >= rule.getMaxNotifications()) {
                System.out.println("Rate limit exceeded for " + type + " notifications sent to user " + userId +
                        " (Rule: " + rule.getMaxNotifications() + " per " + rule.getTimeWindow() + ")");
                return;
            }
        }

        // All rules passed -> send message
        gateway.send(userId, message);
        notificationsHistory.add(new NotificationEvent(type, userId, LocalDateTime.now()));
    }

    private LocalDateTime calculateWindowStart(TimeWindow timeWindow) {
        LocalDateTime now = LocalDateTime.now();
        return switch (timeWindow) {
            case SECOND -> now.minusSeconds(1);
            case MINUTE -> now.minusMinutes(1);
            case HOUR -> now.minusHours(1);
            case DAY -> now.minusDays(1);
        };
    }

    private int countMessagesInWindow(LocalDateTime windowStartTime, String userId, String notificationType) {

        // Get notifications by userId and type
        List<NotificationEvent> userNotifications = notificationsHistory.stream()
                .filter(event -> event.getUserId().equals(userId))
                .filter(event -> event.getNotificationType().equals(notificationType))
                .toList();

        // Count notifications inside time window
        long windowMessageCount = userNotifications.stream()
                .filter(event -> event.getTimestamp().isAfter(windowStartTime))
                .count();

        return (int) windowMessageCount;
    }

}
