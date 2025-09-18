package com.challenge.notifications.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.challenge.notifications.model.RateLimitRule;

@Repository
public interface RateLimitRuleRepository extends JpaRepository<RateLimitRule, Long> {
    List<RateLimitRule> findByNotificationType(String notificationType);
}
