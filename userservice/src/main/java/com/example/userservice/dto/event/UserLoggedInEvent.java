package com.example.userservice.dto.event;

import com.example.userservice.entity.User;

public class UserLoggedInEvent {

    private User user;

    public UserLoggedInEvent(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

}
