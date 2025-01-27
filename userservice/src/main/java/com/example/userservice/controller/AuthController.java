package com.example.userservice.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.userservice.request.CreateUserRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

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

    // 회원 가입
    @PostMapping("/sign-up")
    public void signUp(CreateUserRequest request) {
        log.info("회원 가입 요청");
    }

    // 회원 탈퇴
    @PostMapping("/withdraw")
    public void withdraw() {
        log.info("회원 탈퇴 요청");
    }

    // 토큰 발급
    @PostMapping("/token")
    public void accessToken(@RequestBody String entity) {
        log.info("토큰 발급 요청");
    }

}
