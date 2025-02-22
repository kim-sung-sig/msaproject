package com.example.userservice.dto.event;

import org.springframework.context.ApplicationEvent;

import com.example.userservice.domain.model.UserForSecurity;

public class UserLoggedInEvent extends ApplicationEvent {

    private UserForSecurity user;

    public UserLoggedInEvent(Object source, UserForSecurity user) {
        super(source);
        this.user = user;
    }

    public UserForSecurity getUser() {
        return user;
    }

}
