package com.challenge.notifications.model;

/**
 * Represents the time window for rate-limiting rules.
 * Determines the period over which the maximum notifications are counted.
 */
public enum TimeWindow {
    MINUTE,
    HOUR,
    DAY,
    WEEK
}