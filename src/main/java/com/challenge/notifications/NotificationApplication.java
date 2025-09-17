package com.challenge.notifications;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.challenge.notifications.gateway.Gateway;
import com.challenge.notifications.model.NotificationEvent;
import com.challenge.notifications.model.RateLimitRule;
import com.challenge.notifications.model.TimeWindow;
import com.challenge.notifications.service.NotificationServiceImpl;

public class NotificationApplication {
    public static void main(String[] args) {

        List<RateLimitRule> rules = createExampleRules();
        List<NotificationEvent> notificationsHistory = new ArrayList<>();

        NotificationServiceImpl service = new NotificationServiceImpl(new Gateway(), rules, notificationsHistory);

        showExistingRules(rules);

        Scanner scanner = new Scanner(System.in);

        while (true) {
            String type = readNotificationType(scanner);
            if (type == null)
                break; // exit condition

            String user = readUserId(scanner);
            int count = readMessageCount(scanner);

            sendMultipleNotifications(type, user, count, service);
        }

        scanner.close();
    }

    public static List<RateLimitRule> createExampleRules() {
        List<RateLimitRule> rules = new ArrayList<>();
        rules.add(new RateLimitRule("news", 1, TimeWindow.DAY));
        rules.add(new RateLimitRule("status", 2, TimeWindow.MINUTE));
        rules.add(new RateLimitRule("marketing", 3, TimeWindow.HOUR));
        rules.add(new RateLimitRule("marketing", 1, TimeWindow.MINUTE));
        return rules;
    }

    // ------------------------------
    // Display Helper
    // ------------------------------
    private static void showExistingRules(List<RateLimitRule> rules) {
        System.out.println("Current rules:");
        for (RateLimitRule rule : rules) {
            System.out.printf("- %s: max %d per %s%n",
                    rule.getNotificationType(), rule.getMaxNotifications(), rule.getTimeWindow());
        }
        System.out.println("--------------------");
    }

    // ------------------------------
    // Input Helpers
    // ------------------------------
    private static String readNotificationType(Scanner scanner) {
        System.out.println("Enter notification type (or 'exit'):");
        String type = scanner.nextLine().trim();
        if ("exit".equalsIgnoreCase(type))
            return null;
        return type;
    }

    private static String readUserId(Scanner scanner) {
        System.out.println("Enter userId:");
        return scanner.nextLine().trim();
    }

    private static int readMessageCount(Scanner scanner) {
        int count;
        while (true) {
            System.out.println("Enter number of messages to send (max 6):");
            try {
                count = Integer.parseInt(scanner.nextLine().trim());
                if (count > 6) {
                    System.out.println("Maximum allowed is 6 messages. Sending 6 instead.");
                    return 6;
                } else if (count <= 0) {
                    System.out.println("Number must be at least 1. Try again.");
                    continue;
                }
                return count;
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Try again.");
            }
        }
    }

    // ------------------------------
    // Notification Sending Helper
    // ------------------------------
    private static void sendMultipleNotifications(String type, String user,
            int count, NotificationServiceImpl service) {
        System.out.println("---- Sending " + count + " " + type + " notification(s) to " + user);
        for (int i = 1; i <= count; i++) {
            String message = type + " message " + i;
            service.send(type, user, message);
        }
        System.out.println("---- Done sending " + type + " notifications to " + user + "\n");
    }
}
