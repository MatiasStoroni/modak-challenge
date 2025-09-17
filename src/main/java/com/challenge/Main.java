package com.challenge;

import java.util.ArrayList;
import java.util.List;

import com.challenge.notifications.Gateway;
import com.challenge.notifications.NotificationEvent;
import com.challenge.notifications.NotificationServiceImpl;
import com.challenge.rateLimit.RateLimitRule;
import com.challenge.rateLimit.TimeWindow;

public class Main {
    public static void main(String[] args) {

        // Rules storage
        List<RateLimitRule> rules = new ArrayList<>();

        // Create example rules
        rules.add(new RateLimitRule("news", 4, TimeWindow.DAY));
        rules.add(new RateLimitRule("news", 2, TimeWindow.MINUTE));
        rules.add(new RateLimitRule("status", 2, TimeWindow.MINUTE));
        rules.add(new RateLimitRule("marketing", 3, TimeWindow.HOUR));

        // Notifications history
        List<NotificationEvent> notificationsHistory = new ArrayList<>();

        NotificationServiceImpl service = new NotificationServiceImpl(new Gateway(), rules, notificationsHistory);

        // NEWS
        System.out.println("----------NEWS----------");
        service.send("news", "user", "news 1");
        service.send("news", "user", "news 2");
        service.send("news", "user", "news 3");

        service.send("news", "another user", "news 1");
        service.send("news", "another user", "news 2");
        System.out.println("--------------------");

        // STATUS
        System.out.println("----------STATUS----------");
        service.send("status", "user", "status 1");
        service.send("status", "user", "status 2");
        service.send("status", "user", "status 3");

        service.send("status", "another user", "status 1");
        service.send("status", "another user", "status 2");
        System.out.println("--------------------");

        // MARKETING
        System.out.println("----------MARKETING----------");
        service.send("marketing", "user", "marketing 1");
        service.send("marketing", "user", "marketing 2");
        service.send("marketing", "user", "marketing 3");
        service.send("marketing", "user", "marketing 4");

        service.send("marketing", "another user", "marketing 1");
        service.send("marketing", "another user", "marketing 2");
        service.send("marketing", "another user", "marketing 3");
        service.send("marketing", "another user", "marketing 4");
        service.send("marketing", "another user", "marketing 5");
        System.out.println("--------------------");

        // OTHER
        System.out.println("----------OTHER----------");
        service.send("update", "user", "update 1");
        service.send("update", "user", "update 2");
        service.send("update", "another user", "update 1");
        service.send("update", "another user", "update 2");

        service.send("example", "user", "example 1");

        service.send("example", "another user", "example 1");
        service.send("example", "another user", "example 2");
        System.out.println("--------------------");
    }
}
