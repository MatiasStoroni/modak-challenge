package com.challenge.notifications.service.notificationEvent;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.challenge.notifications.model.NotificationEvent;
import com.challenge.notifications.repository.NotificationEventRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class NotificationEventServiceImpl implements NotificationEventService {

    @Autowired
    private NotificationEventRepository notificationEventRepository;

    @Override
    public NotificationEvent save(NotificationEvent event) {
        return notificationEventRepository.save(event);
    }

    @Override
    public List<NotificationEvent> findAll() {
        return notificationEventRepository.findAll();
    };

    @Override
    public List<NotificationEvent> findByUserIdAndNotificationTypeAndTimestampAfter(String userId,
            String notificationType, LocalDateTime windowStart) {
        return notificationEventRepository.findByUserIdAndNotificationTypeAndTimestampAfter(userId, notificationType,
                windowStart);
    }

}
