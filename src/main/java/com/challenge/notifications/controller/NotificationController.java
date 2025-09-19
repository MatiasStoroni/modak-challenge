package com.challenge.notifications.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.challenge.notifications.dto.NotificationRequestDto;
import com.challenge.notifications.service.notification.NotificationService;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    NotificationService notificationService;

    @PostMapping("/send")
    public ResponseEntity<String> sendNotification(@RequestBody NotificationRequestDto notification) {
        notificationService.send(notification.getNotificationType(), notification.getUserId(), notification.getMessage());
        return ResponseEntity.ok("Notification sent successfully.");
    }
}