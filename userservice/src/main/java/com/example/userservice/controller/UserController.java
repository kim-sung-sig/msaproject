package com.example.userservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.userservice.request.UpdateUserRequest;
import com.example.userservice.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;




@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("")
    public ResponseEntity<?> getUserInfo() {
        log.info("사용자 정보 조회 요청");
        return ResponseEntity.ok().build();
    }

    @PutMapping("")
    public ResponseEntity<?> updateUser(@RequestBody UpdateUserRequest request) {
        log.info("회원 정보 수정 요청: {}", request);
        userService.updateUser(request);
        return ResponseEntity.ok().build();
    }
}
