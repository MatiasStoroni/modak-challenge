package com.challenge.notifications;

import java.time.LocalDateTime;
import java.util.List;

import com.challenge.rateLimit.RateLimitRule;
import com.challenge.rateLimit.TimeWindow;

public class NotificationServiceImpl implements NotificationService {
    private Gateway gateway;
    private List<RateLimitRule> rules;
    private List<NotificationEvent> notificationsHistory;

    public NotificationServiceImpl(Gateway gateway, List<RateLimitRule> rules,
            List<NotificationEvent> notificationsHistory) {
        this.gateway = gateway;
        this.rules = rules;
        this.notificationsHistory = notificationsHistory;
    }

    @Override
    public void send(String type, String userId, String message) {

        // Find the rule for this message type (if any)
        RateLimitRule ruleForType = rules.stream()
                .filter(rule -> rule.getNotificationType().equals(type))
                .findFirst()
                .orElse(null);

        // If there is no rule for this message type -> send notification directly
        if (ruleForType == null) {
            gateway.send(userId, message);
            notificationsHistory.add(new NotificationEvent(type, userId, LocalDateTime.now()));
            return;
        }

        LocalDateTime windowStart = calculateWindowStart(ruleForType.getLimitWindow());
        int windowMessageCount = countMessagesInWindow(windowStart, userId, type);

        if (windowMessageCount < ruleForType.getMaxNotifications()) {
            gateway.send(userId, message);
            notificationsHistory.add(new NotificationEvent(type, userId, LocalDateTime.now()));
        } else {
            System.out.println("Rate limit exceeded for " + type + " notifications sent to user " + userId);
            return;
        }

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

        // Get notifications sent to user
        List<NotificationEvent> userNotifications = notificationsHistory.stream()
                .filter(event -> event.getUserId().equals(userId))
                .filter(event -> event.getNotificationType().equals(notificationType))
                .toList();

        // Count notifications in window
        long windowMessageCount = userNotifications.stream()
                .filter(event -> event.getTimestamp().isAfter(windowStartTime))
                .count();

        return (int) windowMessageCount;
    }

}
