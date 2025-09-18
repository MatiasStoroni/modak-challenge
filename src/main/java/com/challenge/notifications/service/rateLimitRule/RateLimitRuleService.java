package com.challenge.notifications.service.rateLimitRule;

import java.util.List;
import java.util.Optional;

import com.challenge.notifications.model.RateLimitRule;

public interface RateLimitRuleService {

    List<RateLimitRule> findAll();

    Optional<RateLimitRule> findById(Long id);

    RateLimitRule save(RateLimitRule rule);

    RateLimitRule update(Long id, RateLimitRule rule);

    void deleteById(Long id);

    List<RateLimitRule> findByNotificationType(String notificationType);
}
