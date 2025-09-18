package com.challenge.notifications.service.notification;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.challenge.notifications.exception.RateLimitExceededException;
import com.challenge.notifications.model.NotificationEvent;
import com.challenge.notifications.model.RateLimitRule;
import com.challenge.notifications.model.TimeWindow;
import com.challenge.notifications.service.Gateway;
import com.challenge.notifications.service.notificationEvent.NotificationEventService;
import com.challenge.notifications.service.rateLimitRule.RateLimitRuleService;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private final Gateway gateway;

    @Autowired
    private RateLimitRuleService rateLimitRuleService;

    @Autowired
    private NotificationEventService notificationEventService;

    @Override
    public void send(String type, String userId, String message) {

        List<RateLimitRule> applicableRules = rateLimitRuleService.findByNotificationType(type);

        if (applicableRules.isEmpty()) {
            sendNotificationDirectly(userId, message, type);
            return;
        }

        if (isRateLimitExceeded(applicableRules, userId, type)) {
            throw new RateLimitExceededException(
                    "Rate limit exceeded for " + type + " notifications sent to user " + userId);
        }

        sendNotificationDirectly(userId, message, type);
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
        int currentMessageCount = countMessagesInWindow(userId, type, windowStart);
        return currentMessageCount >= rule.getMaxNotifications();
    }

    private LocalDateTime calculateWindowStart(TimeWindow timeWindow) {
        LocalDateTime now = LocalDateTime.now();
        return switch (timeWindow) {
            case MINUTE -> now.minusMinutes(1);
            case HOUR -> now.minusHours(1);
            case DAY -> now.minusDays(1);
            case WEEK -> now.minusDays(7);
        };
    }

    private int countMessagesInWindow(String userId, String notificationType, LocalDateTime windowStartTime) {
        return (int) notificationEventService
                .findByUserIdAndNotificationTypeAndTimestampAfter(userId, notificationType, windowStartTime)
                .size();
    }

    private void sendNotificationDirectly(String userId, String message, String type) {
        gateway.send(userId, message);
        recordNotificationEvent(userId, type);
    }

    private void recordNotificationEvent(String userId, String type) {
        NotificationEvent event = new NotificationEvent();
        event.setNotificationType(type);
        event.setUserId(userId);
        event.setTimestamp(LocalDateTime.now());
        notificationEventService.save(event);
    }

}
