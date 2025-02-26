package com.example.userservice.application.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.userservice.domain.entity.User;
import com.example.userservice.domain.exception.UserNotFoundException;
import com.example.userservice.domain.repository.profile.UserProfilePictureRepository;
import com.example.userservice.domain.repository.user.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserProfilePictureService {

    private final UserRepository userRepository;
    private final UserProfilePictureRepository userProfilePictureRepository;

    // 프로필 사진 등록
    public void createProfilePicture(Long userId, MultipartFile file) {
        log.info("createProfilePicture");
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException());

    }
}
