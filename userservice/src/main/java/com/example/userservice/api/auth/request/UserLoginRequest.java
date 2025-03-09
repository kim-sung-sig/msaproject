package com.example.userservice.api.auth.request;

public record UserLoginRequest(
    String username,
    String password
) {}
