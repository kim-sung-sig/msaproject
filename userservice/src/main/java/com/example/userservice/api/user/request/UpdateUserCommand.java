package com.example.userservice.api.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdateUserCommand(
    // 확인용
    String currentPassword,

    // 변경 대상
    @Size(min = 2, max = 10)
    String name,

    @Size(min = 2, max = 16)
    String nickName,

    @Email
    String email,

    @Size(min = 8, max = 20)
    String newPassword
) {}
