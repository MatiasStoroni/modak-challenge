package com.challenge.notifications.gateway;

public class Gateway {
    public void send(String userId, String message) {

        System.out.println("Sending message to user " + userId.toUpperCase() + ", message: " + message);

    }
}
