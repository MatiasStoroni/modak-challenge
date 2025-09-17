package com.challenge.notifications;

public class Gateway {
    void send(String userId, String message) {

        System.out.println("Sending message to user " + userId.toUpperCase() + ", message: " + message);

    }
}
