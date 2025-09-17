package com.challenge.notifications.service;

import java.time.LocalDateTime;
import java.util.List;

import com.challenge.notifications.gateway.Gateway;
import com.challenge.notifications.model.NotificationEvent;
import com.challenge.notifications.model.RateLimitRule;
import com.challenge.notifications.model.TimeWindow;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final Gateway gateway;
    private final List<RateLimitRule> rules;
    private final List<NotificationEvent> notificationsHistory;

    @Override
    public void send(String type, String userId, String message) {

        List<RateLimitRule> applicableRules = findRulesForNotificationType(type);

        if (applicableRules.isEmpty()) {
            sendNotificationDirectly(userId, message, type);
            return;
        }

        if (isRateLimitExceeded(applicableRules, userId, type)) {
            System.out.println("Rate limit exceeded for " + type + " notifications sent to user " + userId);
            return;
        }

        sendNotificationDirectly(userId, message, type);
    }

    private List<RateLimitRule> findRulesForNotificationType(String type) {
        return rules.stream()
                .filter(rule -> rule.getNotificationType().equals(type))
                .toList();
    }

    private boolean isRateLimitExceeded(List<RateLimitRule> applicableRules, String userId, String type) {
        for (RateLimitRule rule : applicableRules) {
            if (isRuleViolated(rule, userId, type)) {
                return true;
            }
        }
        return false;
    }

    private boolean isRuleViolated(RateLimitRule rule, String userId, String type) {
        LocalDateTime windowStart = calculateWindowStart(rule.getTimeWindow());
        int currentMessageCount = countMessagesInWindow(windowStart, userId, type);
        return currentMessageCount >= rule.getMaxNotifications();
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
        return (int) notificationsHistory.stream()
                .filter(event -> event.getUserId().equals(userId))
                .filter(event -> event.getNotificationType().equals(notificationType))
                .filter(event -> event.getTimestamp().isAfter(windowStartTime))
                .count();
    }

    private void sendNotificationDirectly(String userId, String message, String type) {
        gateway.send(userId, message);
        recordNotificationEvent(userId, type);
    }

    private void recordNotificationEvent(String userId, String type) {
        NotificationEvent event = new NotificationEvent(type, userId, LocalDateTime.now());
        notificationsHistory.add(event);
    }

}
