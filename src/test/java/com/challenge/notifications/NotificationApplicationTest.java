package com.challenge.notifications;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.challenge.notifications.model.RateLimitRule;
import com.challenge.notifications.model.TimeWindow;

public class NotificationApplicationTest {

    @Test
    public void testCreateExampleRules() {
        List<RateLimitRule> rules = NotificationApplication.createExampleRules();

        assertEquals(4, rules.size());

        // Check rules individually
        RateLimitRule rule1 = rules.get(0);
        assertEquals("news", rule1.getNotificationType());
        assertEquals(1, rule1.getMaxNotifications());
        assertEquals(TimeWindow.DAY, rule1.getTimeWindow());

        RateLimitRule rule2 = rules.get(1);
        assertEquals("status", rule2.getNotificationType());
        assertEquals(2, rule2.getMaxNotifications());
        assertEquals(TimeWindow.MINUTE, rule2.getTimeWindow());

        RateLimitRule rule3 = rules.get(2);
        assertEquals("marketing", rule3.getNotificationType());
        assertEquals(3, rule3.getMaxNotifications());
        assertEquals(TimeWindow.HOUR, rule3.getTimeWindow());

        RateLimitRule rule4 = rules.get(3);
        assertEquals("marketing", rule4.getNotificationType());
        assertEquals(1, rule4.getMaxNotifications());
        assertEquals(TimeWindow.MINUTE, rule4.getTimeWindow());
    }

}
