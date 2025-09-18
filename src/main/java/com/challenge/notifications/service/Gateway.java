package com.challenge.notifications.service;

import org.springframework.stereotype.Service;

@Service
public class Gateway {
    public void send(String userId, String message) {
        System.out.println("Sending message to user " + userId.toUpperCase() + ", message: " + message);
    }
}
