package com.challenge.notifications.service;

public interface NotificationService {
    void send(String type, String userId, String message);
}
