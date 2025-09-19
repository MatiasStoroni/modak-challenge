package com.challenge.notifications.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.challenge.notifications.dto.CreateRuleDto;
import com.challenge.notifications.model.RateLimitRule;
import com.challenge.notifications.service.rateLimitRule.RateLimitRuleService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/rules")
public class RateLimitRuleController {

    @Autowired
    RateLimitRuleService rateLimitRuleService;

    @GetMapping
    public ResponseEntity<List<RateLimitRule>> getAllRateLimitRules() {
        return ResponseEntity.ok(rateLimitRuleService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RateLimitRule> getRateLimitRule(@PathVariable Long id) {
        return rateLimitRuleService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createRateLimitRule(@Valid @RequestBody CreateRuleDto dto) {
        RateLimitRule newRule = new RateLimitRule();
        newRule.setNotificationType(dto.getNotificationType());
        newRule.setMaxNotifications(dto.getMaxNotifications());
        newRule.setTimeWindow(dto.getTimeWindow());

        RateLimitRule created = rateLimitRuleService.save(newRule);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRateLimitRule(@PathVariable Long id,
            @Valid @RequestBody RateLimitRule rule) {
        return ResponseEntity.ok(rateLimitRuleService.update(id, rule));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRateLimitRule(@PathVariable Long id) {
        rateLimitRuleService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}