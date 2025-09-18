package com.challenge.notifications.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.challenge.notifications.model.NotificationEvent;
import com.challenge.notifications.service.notificationEvent.NotificationEventServiceImpl;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/events")
public class NotificationEventController {

    @Autowired
    NotificationEventServiceImpl notificationEventService;

    @GetMapping
    public ResponseEntity<List<NotificationEvent>> findAllNotificationEvents() {
        return ResponseEntity.ok(notificationEventService.findAll());
    }

    @PostMapping
    public ResponseEntity<?> createNotificationEvent(@Valid @RequestBody NotificationEvent notificationEvent) {
        NotificationEvent created = notificationEventService.save(notificationEvent);
        return ResponseEntity.ok(created);
    }

}