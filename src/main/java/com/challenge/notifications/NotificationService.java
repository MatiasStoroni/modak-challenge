package com.challenge.notifications;

public interface NotificationService {
    void send(String type, String userId, String message);
}
