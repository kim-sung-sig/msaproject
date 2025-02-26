package com.example.userservice.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.userservice.application.service.UserProfilePictureService;
import com.example.userservice.common.config.securiry.dto.CustomUserDetails;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping("/api/v1/user/{userId}/profile")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfilePictureService userProfilePictureService;

    @GetMapping("/images")
    public ResponseEntity<Void> getUserProfileImage(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long userId) {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/details")
    public ResponseEntity<Void> getUserProfilesDetail(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long userId) {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{profileId}/details")
    public ResponseEntity<Void> getUserProfileDetail(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long userId) {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{profileId}/download")
    public ResponseEntity<Void> getUserProfileDetails(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long userId) {
        return ResponseEntity.ok().build();
    }

}
