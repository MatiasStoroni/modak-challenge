// package com.challenge.notifications.model;

// import static org.junit.Assert.assertEquals;

// import java.time.LocalDateTime;

// import org.junit.Test;
// /**
//  * Unit tests for model classes.
//  * 
//  * These tests are designed to cover:
//  * - Getters and setters of RateLimitRule and NotificationEvent
//  * - All-arguments constructors of RateLimitRule and NotificationEvent
//  */
// public class ModelTest {
//     @Test
//     public void testRateLimitRuleGettersAndSetters() {
//         RateLimitRule rule = new RateLimitRule();
//         rule.setNotificationType("news");
//         rule.setMaxNotifications(3);
//         rule.setTimeWindow(TimeWindow.HOUR);

//         assertEquals("news", rule.getNotificationType());
//         assertEquals(3, rule.getMaxNotifications());
//         assertEquals(TimeWindow.HOUR, rule.getTimeWindow());
//     }

//     @Test
//     public void testRateLimitRuleAllArgsConstructor() {
//         RateLimitRule rule = new RateLimitRule("status", 2, TimeWindow.MINUTE);
//         assertEquals("status", rule.getNotificationType());
//         assertEquals(2, rule.getMaxNotifications());
//         assertEquals(TimeWindow.MINUTE, rule.getTimeWindow());
//     }

//     @Test
//     public void testNotificationEventGettersAndSetters() {
//         NotificationEvent event = new NotificationEvent();
//         LocalDateTime now = LocalDateTime.now();
//         event.setUserId("user1");
//         event.setNotificationType("news");
//         event.setTimestamp(now);

//         assertEquals("user1", event.getUserId());
//         assertEquals("news", event.getNotificationType());
//         assertEquals(now, event.getTimestamp());
//     }

//     @Test
//     public void testNotificationEventAllArgsConstructor() {
//         LocalDateTime now = LocalDateTime.now();
//         NotificationEvent event = new NotificationEvent("status", "user2", now);

//         assertEquals("status", event.getNotificationType());
//         assertEquals("user2", event.getUserId());
//         assertEquals(now, event.getTimestamp());
//     }
// }
