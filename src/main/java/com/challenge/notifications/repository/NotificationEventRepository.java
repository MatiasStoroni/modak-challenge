package com.challenge.notifications.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.challenge.notifications.model.NotificationEvent;

@Repository
public interface NotificationEventRepository extends JpaRepository<NotificationEvent, Long> {
    int countByUserIdAndNotificationTypeAndTimestampAfter(
            String userId,
            String notificationType,
            LocalDateTime timestamp);
}
