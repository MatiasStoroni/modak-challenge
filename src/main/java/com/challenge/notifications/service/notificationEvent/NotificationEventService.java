package com.challenge.notifications.service.notificationEvent;

import java.time.LocalDateTime;
import java.util.List;

import com.challenge.notifications.model.NotificationEvent;

public interface NotificationEventService {

    NotificationEvent save(NotificationEvent notificationEvent);

    List<NotificationEvent> findAll();

    List<NotificationEvent> findByUserIdAndNotificationTypeAndTimestampAfter(
            String userId,
            String notificationType,
            LocalDateTime timestamp);

}
