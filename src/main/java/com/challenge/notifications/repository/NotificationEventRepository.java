package com.challenge.notifications.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.challenge.notifications.model.NotificationEvent;

@Repository
public interface NotificationEventRepository extends JpaRepository<NotificationEvent, Long> {
    List<NotificationEvent> findByUserIdAndNotificationTypeAndTimestampAfter(
            String userId,
            String notificationType,
            LocalDateTime timestamp);

}
