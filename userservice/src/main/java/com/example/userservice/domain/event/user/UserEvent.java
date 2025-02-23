package com.example.userservice.domain.event.user;

import java.util.UUID;

import org.springframework.context.ApplicationEvent;

import com.example.userservice.common.enums.EventType;
import com.example.userservice.domain.entity.User;
import com.example.userservice.domain.entity.User.UserRole;
import com.example.userservice.domain.entity.User.UserStatus;

import lombok.Getter;

@Getter
public class UserEvent extends ApplicationEvent {

    private final Long id;
    private final UUID uuid;
    private final UserRole role;
    private final UserStatus status;

    private final String nickName;

    private final EventType eventType;

    public UserEvent(Object source, User user, EventType eventType) {
        super(source);
        this.id = user.getId();
        this.uuid = user.getUuid();
        this.role = user.getRole();
        this.status = user.getStatus();

        this.nickName = user.getNickName();

        this.eventType = eventType;
    }

}
