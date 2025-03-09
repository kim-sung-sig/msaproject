package com.example.userservice.api.auth;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.userservice.api.auth.request.UserLoginRequest;
import com.example.userservice.api.user.request.CreateUserCommand;
import com.example.userservice.application.components.JwtUtil;
import com.example.userservice.application.service.auth.AuthService;
import com.example.userservice.application.service.auth.JwtTokenResponse;
import com.example.userservice.common.constants.ConstantsUtil;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // 회원 가입
    @PostMapping("/signup")
    public void signUp(CreateUserCommand request) {
        log.info("회원 가입 요청");
    }

    /**
     * Jwt AccessToken 발급 및 refreshToken 발급 (username, password)
     * @param loginRequest
     */
    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody UserLoginRequest loginRequest, HttpServletResponse response) {
        JwtTokenResponse token = authService.login(loginRequest);
        setRefreshTokenCookie(response, token.refreshToken());

        return ResponseEntity.noContent()
                .header(HttpHeaders.AUTHORIZATION, JwtUtil.BEARER_PREFIX + token.accessToken())
                .build();
    }

    /**
     * Jwt AccessToken 발급 및 RefreshToken 발급 (refreshToken)
     * @param entity
     */
    @PostMapping("/token/refresh")
    public ResponseEntity<Void> refreshToken(@CookieValue(value = "refreshToken", required = true) String refreshToken, HttpServletResponse response) {
        JwtTokenResponse token = authService.tokenRefresh(refreshToken);
        setRefreshTokenCookie(response, token.refreshToken());

        return ResponseEntity.noContent()
                .header(HttpHeaders.AUTHORIZATION, JwtUtil.BEARER_PREFIX + token.accessToken())
                .build();
    }

    // 비밀 번호 재설정 요청
    @PostMapping("/password-reset-request")
    public void passwordResetRequest() {
        log.info("비밀번호 재설정 요청");
    }

    // 비밀 번호 재설정
    @PostMapping("/password-reset")
    public void passwordReset() {
        log.info("비밀번호 재설정");
    }

    // 회원 탈퇴
    @PostMapping("/withdraw")
    public void withdraw() {
        log.info("회원 탈퇴 요청");
    }

    private void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        // cookie.setSecure(true);  // HTTPS 연결에서만 전송
        cookie.setPath("/");  // 모든 경로에서 접근 가능
        cookie.setMaxAge((int) ConstantsUtil.REFRESH_TOKEN_TTL);  // 7일 동안 유효
        response.addCookie(cookie);
    }

}
