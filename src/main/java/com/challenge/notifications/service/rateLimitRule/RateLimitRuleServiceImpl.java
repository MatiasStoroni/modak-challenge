package com.challenge.notifications.service.rateLimitRule;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.challenge.notifications.model.RateLimitRule;
import com.challenge.notifications.repository.RateLimitRuleRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class RateLimitRuleServiceImpl implements RateLimitRuleService {

    @Autowired
    private RateLimitRuleRepository rateLimitRuleRepository;

    @Override
    public RateLimitRule save(RateLimitRule rateLimitRule) {
        return rateLimitRuleRepository.save(rateLimitRule);
    }

    @Override
    public Optional<RateLimitRule> findById(Long id) {
        return rateLimitRuleRepository.findById(id);
    }

    @Override
    public List<RateLimitRule> findAll() {
        return rateLimitRuleRepository.findAll();
    }

    @Override
    public RateLimitRule update(Long id, RateLimitRule rateLimitRule) {
        RateLimitRule existingRateLimitRule = rateLimitRuleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rule not found"));

        if (rateLimitRule.getNotificationType() != null) {
            existingRateLimitRule.setNotificationType(rateLimitRule.getNotificationType());
        }

        if (rateLimitRule.getMaxNotifications() != null) {
            existingRateLimitRule.setMaxNotifications(rateLimitRule.getMaxNotifications());
        }

        if (rateLimitRule.getTimeWindow() != null) {
            existingRateLimitRule.setTimeWindow(rateLimitRule.getTimeWindow());
        }

        return rateLimitRuleRepository.save(existingRateLimitRule);
    }

    @Override
    public void deleteById(Long id) {
        rateLimitRuleRepository.deleteById(id);
    }

    @Override
    public List<RateLimitRule> findByNotificationType(String notificationType) {
        return rateLimitRuleRepository.findByNotificationType(notificationType);
    }
}
