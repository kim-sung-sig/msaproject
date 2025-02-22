package com.example.userservice.domain.event;

import java.util.UUID;

import org.springframework.context.ApplicationEvent;

import com.example.userservice.domain.entity.User;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UserDeletedEvent extends ApplicationEvent {

    private Long userId;
    private UUID userUuid;

    public UserDeletedEvent(Object source, User user) {
        super(source);
        this.userId = user.getId();
        this.userUuid = user.getUuid();
    }

}
