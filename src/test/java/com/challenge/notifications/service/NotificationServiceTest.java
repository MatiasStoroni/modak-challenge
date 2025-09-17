package com.challenge.notifications.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.challenge.notifications.gateway.Gateway;
import com.challenge.notifications.model.NotificationEvent;
import com.challenge.notifications.model.RateLimitRule;
import com.challenge.notifications.model.TimeWindow;

/**
 * Naming Convention for Tests:
 * 
 * Each test method is named using the pattern:
 * 
 * methodUnderTest_scenario_expectedBehavior
 * 
 * For example:
 * send_NoRules_SendsNotification -> tests send() when there are no rules
 */
public class NotificationServiceTest {
    private Gateway gateway;
    private List<NotificationEvent> notificationsHistory;
    private List<RateLimitRule> rules;
    private NotificationServiceImpl service;

    @Before // Initializations before each test
    public void setUp() {
        gateway = Mockito.mock(Gateway.class);
        notificationsHistory = new ArrayList<>();
        rules = new ArrayList<>();

        service = new NotificationServiceImpl(gateway, rules, notificationsHistory);
    }

    @Test
    public void send_NoRules_SendsNotification() {

        service.send("SMS", "USER", "Welcome");

        verify(gateway, times(1)).send("USER", "Welcome");
        assertEquals(1, notificationsHistory.size());
    }

    @Test
    public void send_WithRuleWithinLimit_AllowsSend() {

        rules.add(new RateLimitRule("EMAIL", 2, TimeWindow.MINUTE));
        service = new NotificationServiceImpl(gateway, rules, notificationsHistory);

        service.send("EMAIL", "USER", "Email 1");
        service.send("EMAIL", "USER", "Email 2");

        verify(gateway, times(2)).send(eq("USER"), anyString());
        assertEquals(2, notificationsHistory.size());
    }

    @Test
    public void send_WithRuleExceeded_DoesNotSendBeyondLimit() {

        rules.add(new RateLimitRule("STATUS", 3, TimeWindow.MINUTE));
        service = new NotificationServiceImpl(gateway, rules, notificationsHistory);

        service.send("STATUS", "USER", "Email 1");
        service.send("STATUS", "USER", "Email 2");
        service.send("STATUS", "USER", "Email 3");
        service.send("STATUS", "USER", "Email 4"); // should be blocked

        verify(gateway, times(3)).send(eq("USER"), anyString());
        assertEquals(3, notificationsHistory.size());
    }

    @Test
    public void send_DifferentUser_NotBlockedByAnotherUsersMessages() {

        rules.add(new RateLimitRule("NEWS", 1, TimeWindow.MINUTE));
        service = new NotificationServiceImpl(gateway, rules, notificationsHistory);

        service.send("NEWS", "USER", "News 1");
        service.send("NEWS", "OTHER_USER", "News 1");
        service.send("NEWS", "USER", "News 2"); // should be blocked for "USER"

        verify(gateway, times(1)).send(eq("USER"), anyString());
        verify(gateway, times(1)).send(eq("OTHER_USER"), anyString());
        assertEquals(2, notificationsHistory.size());
    }

    @Test
    public void send_OldEventsOutsideWindow_DoNotBlockWithinTimeWindow() {

        rules.add(new RateLimitRule("NEWS", 1, TimeWindow.DAY));
        service = new NotificationServiceImpl(gateway, rules, notificationsHistory);
        // old event outside 1 day window
        notificationsHistory.add(new NotificationEvent("NEWS", "USER", LocalDateTime.now().minusDays(1)));

        service.send("NEWS", "USER", "News");

        verify(gateway, times(1)).send(eq("USER"), anyString());
        assertEquals(2, notificationsHistory.size()); // old one preserved plus new
    }

}
