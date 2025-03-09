package com.example.userservice.application.components;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class EventPublisher {

    private static ApplicationEventPublisher applicationEventPublisher;

    public EventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        log.debug("EventPublisher created");
        EventPublisher.applicationEventPublisher = applicationEventPublisher;
        log.debug("EventPublisher.applicationEventPublisher: {}", EventPublisher.applicationEventPublisher);
    }

    @PostConstruct
    private void init() {
        log.debug("EventPublisher initialized");
        if (applicationEventPublisher == null) {
            throw new RuntimeException("applicationEventPublisher is null");
        }
    }

    public static void publish(Object event) {
        if (applicationEventPublisher == null) {
            return;
        }
        applicationEventPublisher.publishEvent(event);
    }

}
