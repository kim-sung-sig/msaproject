package com.example.userservice.domain.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.example.userservice.domain.entity.User.UserRole;
import com.example.userservice.domain.entity.User.UserStatus;

public record UserForSecurity(
    Long id,

    String username,
    String password,
    UserRole role,

    UserStatus status,
    int loginFailCount,

    LocalDateTime lastLoginAt,
    LocalDateTime createdAt
) implements Serializable {}
