package com.challenge.notifications.service.notification;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.challenge.notifications.model.NotificationEvent;
import com.challenge.notifications.model.RateLimitRule;
import com.challenge.notifications.model.TimeWindow;
import com.challenge.notifications.service.Gateway;
import com.challenge.notifications.service.notificationEvent.NotificationEventServiceImpl;
import com.challenge.notifications.service.rateLimitRule.RateLimitRuleServiceImpl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private final Gateway gateway;

    @Autowired
    private RateLimitRuleServiceImpl ruleService;

    @Autowired
    private NotificationEventServiceImpl eventService;

    @Override
    public void send(String type, String userId, String message) {

        List<RateLimitRule> applicableRules = ruleService.findByNotificationType(type);

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
        int currentMessageCount = eventService.countByUserAndTypeAfterTime(userId, type, windowStart);
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

    private void sendNotificationDirectly(String userId, String message, String type) {
        gateway.send(userId, message);
        recordNotificationEvent(userId, type);
    }

    private void recordNotificationEvent(String userId, String type) {
        NotificationEvent event = new NotificationEvent();
        event.setNotificationType(type);
        event.setUserId(userId);
        event.setTimestamp(LocalDateTime.now());
        eventService.save(event);
    }

}
