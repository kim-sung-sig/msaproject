package com.example.userservice.domain.event.profile;

import java.util.UUID;

import org.springframework.context.ApplicationEvent;

import com.example.userservice.common.enums.EventType;
import com.example.userservice.domain.entity.User;
import com.example.userservice.domain.entity.UserProfilePicture;

import lombok.Getter;

@Getter
public class UserProfilePictureEvent extends ApplicationEvent {

    // user
    private final Long userId;
    private final UUID uuid;

    // profile
    private final Long profileId;
    private final UUID profileUuid;
    private final String fileUrl;
    private final String status;

    // event 상태
    private final EventType eventType;

    public UserProfilePictureEvent(Object source, UserProfilePicture profile, EventType eventType) {
        super(source);
        User user = profile.getUser();
        this.userId = user.getId();
        this.uuid = profile.getUuid();

        this.profileId = profile.getId();
        this.profileUuid = profile.getUuid();
        this.fileUrl = profile.getFileUrl();
        this.status = profile.getStatus().name();

        this.eventType = eventType;
    }

}
