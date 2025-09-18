package com.challenge.notifications.service.notificationEvent;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.challenge.notifications.model.NotificationEvent;

public interface NotificationEventService {

    List<NotificationEvent> findAll();

    Optional<NotificationEvent> findById(Long id);

    NotificationEvent save(NotificationEvent notificationEvent);

    void deleteById(Long id);

    int countByUserAndTypeAfterTime(String userId, String notificationType, LocalDateTime timestamp);
}
