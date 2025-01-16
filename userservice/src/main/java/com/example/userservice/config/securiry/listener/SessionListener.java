package com.example.userservice.config.securiry.listener;

import org.springframework.stereotype.Component;

import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@WebListener
@RequiredArgsConstructor
public class SessionListener implements HttpSessionListener{

    // private final ApplicationEventPublisher publisher;

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        log.debug("Session created");
        HttpSessionListener.super.sessionCreated(se);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        log.debug("Session Destroyed");
        if (se.getSession().getAttribute("user") != null) {
            // CustomUserDatails userDatails = (CustomUserDatails) se.getSession().getAttribute("user");
            // User user = userDatails.getUser();

            // 세션이 종료되기 전에 필요한 작업을 미리 처리
            // publisher.publishEvent(new SessionDestroyedEvent(se, user));
        }
    }
}
