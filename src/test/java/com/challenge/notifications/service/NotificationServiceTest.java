package com.challenge.notifications.service;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.challenge.notifications.exception.RateLimitExceededException;
import com.challenge.notifications.model.NotificationEvent;
import com.challenge.notifications.model.RateLimitRule;
import com.challenge.notifications.model.TimeWindow;
import com.challenge.notifications.service.notification.NotificationService;
import com.challenge.notifications.service.notification.NotificationServiceImpl;
import com.challenge.notifications.service.notificationEvent.NotificationEventService;
import com.challenge.notifications.service.rateLimitRule.RateLimitRuleService;

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

    @Mock
    private Gateway gateway;

    @Mock
    private RateLimitRuleService ruleService;

    @Mock
    private NotificationEventService eventService;

    private NotificationService service;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new NotificationServiceImpl(gateway, ruleService, eventService);
    }

    @Test
    public void send_NoRules_SendsNotification() {

        when(ruleService.findByNotificationType("sms")).thenReturn(new ArrayList<>());

        service.send("SMS", "user", "Welcome");

        verify(gateway, times(1)).send("user", "Welcome");
        verify(eventService, times(1)).save(any(NotificationEvent.class));
    }

    @Test
    public void send_WithRuleWithinLimit_AllowsSend() {

        List<RateLimitRule> rules = new ArrayList<>();
        rules.add(new RateLimitRule(1L, "STATUS", 2, TimeWindow.MINUTE));

        when(ruleService.findByNotificationType("STATUS"))
                .thenReturn(rules);

        when(eventService.findByUserIdAndNotificationTypeAndTimestampAfter(eq("user"), eq("STATUS"),
                any(LocalDateTime.class)))
                .thenReturn(new ArrayList<>())
                .thenReturn(createEventList("STATUS", "user", 1));

        service.send("STATUS", "user", "Status message 1");
        service.send("STATUS", "user", "Status message 2");

        verify(gateway, times(2)).send(eq("user"), anyString());
        verify(eventService, times(2)).save(any(NotificationEvent.class));
    }

    @Test
    public void send_WithRuleExceeded_ThrowsException() {
        List<RateLimitRule> rules = new ArrayList<>();
        rules.add(new RateLimitRule(1L, "UPDATE", 2, TimeWindow.HOUR));

        when(ruleService.findByNotificationType("UPDATE"))
                .thenReturn(rules);

        when(eventService.findByUserIdAndNotificationTypeAndTimestampAfter(eq("user"), eq("UPDATE"),
                any(LocalDateTime.class)))
                .thenReturn(new ArrayList<>())
                .thenReturn(createEventList("UPDATE", "user", 1))
                .thenReturn(createEventList("UPDATE", "user", 2));

        service.send("UPDATE", "user", "Update message 1");
        service.send("UPDATE", "user", "Update message 2");

        assertThrows(RateLimitExceededException.class, () -> {
            service.send("UPDATE", "user", "Update message 3");
        });

        verify(gateway, times(2)).send(eq("user"), anyString());
        verify(eventService, times(2)).save(any(NotificationEvent.class));
    }

    @Test
    public void send_DifferentUser_NotBlockedByAnotherUsersMessages() {
        List<RateLimitRule> rules = new ArrayList<>();
        rules.add(new RateLimitRule(1L, "NEWS", 1, TimeWindow.DAY));

        when(ruleService.findByNotificationType("NEWS"))
                .thenReturn(rules);

        when(eventService.findByUserIdAndNotificationTypeAndTimestampAfter(eq("user"), eq("NEWS"),
                any(LocalDateTime.class)))
                .thenReturn(new ArrayList<>())
                .thenReturn(createEventList("NEWS", "user", 1));

        when(eventService.findByUserIdAndNotificationTypeAndTimestampAfter(eq("other_user"), eq("NEWS"),
                any(LocalDateTime.class)))
                .thenReturn(new ArrayList<>());

        service.send("NEWS", "user", "News 1");
        service.send("NEWS", "other_user", "News 1");

        assertThrows(RateLimitExceededException.class, () -> {
            service.send("NEWS", "user", "News 2");
        });

        verify(gateway, times(1)).send(eq("user"), anyString());
        verify(gateway, times(1)).send(eq("other_user"), anyString());
        verify(eventService, times(2)).save(any(NotificationEvent.class));
    }

    @Test
    public void send_WhenOneRuleAllowsButAnotherBlocks_ShouldRespectStricterRule() {
        List<RateLimitRule> rules = new ArrayList<>();
        rules.add(new RateLimitRule(1L, "MARKETING", 1, TimeWindow.MINUTE));
        rules.add(new RateLimitRule(2L, "MARKETING", 3, TimeWindow.HOUR));

        when(ruleService.findByNotificationType("MARKETING"))
                .thenReturn(rules);

        List<NotificationEvent> recentEventList = new ArrayList<>();
        recentEventList.add(new NotificationEvent(1L, "MARKETING", "user", LocalDateTime.now().minusSeconds(30)));

        when(eventService.findByUserIdAndNotificationTypeAndTimestampAfter(eq("user"), eq("MARKETING"),
                any(LocalDateTime.class)))
                .thenReturn(recentEventList);

        assertThrows(RateLimitExceededException.class, () -> {
            service.send("MARKETING", "user", "BlockedMsg");
        });

        verify(gateway, never()).send(eq("user"), anyString());
        verify(eventService, never()).save(any(NotificationEvent.class));
    }

    // Helper function for creating event lists
    private List<NotificationEvent> createEventList(String notificationType, String userId, int elements) {
        List<NotificationEvent> events = new ArrayList<>();
        for (int i = 1; i <= elements; i++) {
            events.add(new NotificationEvent((long) i, notificationType, userId, LocalDateTime.now()));
        }
        return events;
    }
}