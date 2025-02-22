package com.example.userservice.domain.event;

import org.springframework.context.ApplicationEvent;

import com.example.userservice.domain.entity.User;

import lombok.Getter;

@Getter
public class UserCreatedEvent extends ApplicationEvent {

    private User user;

    public UserCreatedEvent(Object source, User user) {
        super(source);
        this.user = user;
    }

}
