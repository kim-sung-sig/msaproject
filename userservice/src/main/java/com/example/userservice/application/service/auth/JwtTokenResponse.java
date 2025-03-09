package com.example.userservice.application.service.auth;

public record JwtTokenResponse(
    String accessToken,
    String refreshToken
) {}
