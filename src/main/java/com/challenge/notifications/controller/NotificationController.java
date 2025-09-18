package com.challenge.notifications.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.challenge.notifications.service.notification.NotificationServiceImpl;

import lombok.Getter;
import lombok.Setter;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    NotificationServiceImpl notificationService;

    @PostMapping
    public void send(@RequestBody Notification notification) {
        notificationService.send(
                notification.getNotificationType(),
                notification.getUserId(),
                notification.getMessage());
    }
}

@Getter
@Setter
class Notification {
    private String notificationType;
    private String userId;
    private String message;
}
