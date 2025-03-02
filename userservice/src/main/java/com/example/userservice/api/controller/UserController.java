package com.example.userservice.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.userservice.api.request.user.CreateUserCommand;
import com.example.userservice.api.request.user.UpdateUserCommand;
import com.example.userservice.application.service.UserService;
import com.example.userservice.common.config.securiry.dto.CustomUserDetails;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<Void> getUserList(
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        log.info("사용자 정보 조회 요청");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Void> getUser(
        @AuthenticationPrincipal CustomUserDetails userDetails
        , @PathVariable Long userId
    ) {
        log.info("사용자 정보 조회 요청");
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Void> createUser(
        @AuthenticationPrincipal CustomUserDetails userDetails
        , @RequestBody CreateUserCommand command
    ) {
        log.info("회원 가입 요청: {}", command);
        userService.createUser(command);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Void> updateUser(
        @AuthenticationPrincipal CustomUserDetails userDetails
        , @PathVariable Long userId
        , @RequestBody UpdateUserCommand command
    ) {
        log.info("회원 정보 수정 요청: {}", command);
        userDetails.getUser();
        userService.updateUser(command);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(
        @AuthenticationPrincipal CustomUserDetails userDetails
        , @PathVariable Long userId
    ) {
        log.info("회원 탈퇴 요청");
        return ResponseEntity.ok().build();
    }
}
