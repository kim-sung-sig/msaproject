package com.example.userservice.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.userservice.application.service.UserProfilePictureService;
import com.example.userservice.common.config.securiry.dto.CustomUserDetails;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserProfilePictureService userProfilePictureService;

    @PostMapping
    public ResponseEntity<Void> createProfile(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{profileId}")
    public ResponseEntity<Void> updateProfile(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{profileId}")
    public ResponseEntity<Void> deleteProfile(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok().build();
    }

}
