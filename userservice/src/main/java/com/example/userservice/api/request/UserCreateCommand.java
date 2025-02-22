package com.example.userservice.api.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserCreateCommand(
    @NotNull
    @Size(min = 8, max = 20)
    String username,

    @NotNull
    @Size(min = 8, max = 20)
    String password,

    @NotNull
    @Size(min = 2, max = 10)
    String name,

    @NotNull
    @Size(min = 2, max = 16)
    String nickName,

    @Email
    @NotNull
    String email
) {}
