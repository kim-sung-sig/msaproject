package com.example.userservice.domain.model;

import java.time.LocalDateTime;

import com.example.userservice.domain.enums.UserRole;
import com.example.userservice.domain.enums.UserStatus;

public record UserForSecurity(
    Long id,

    String username,
    String password,
    UserRole role,

    UserStatus status,
    int loginFailCount,

    LocalDateTime lastLoginAt,
    LocalDateTime createdAt
) {}
