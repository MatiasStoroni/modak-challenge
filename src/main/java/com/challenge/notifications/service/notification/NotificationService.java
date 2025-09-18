package com.challenge.notifications.service.notification;

public interface NotificationService {
    void send(String type, String userId, String message);
}
