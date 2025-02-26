package com.example.userservice.domain.exception;

import jakarta.persistence.EntityNotFoundException;

public class UserProfilePictureNotFoundException extends EntityNotFoundException {

    private static final String DEFAULT_MESSAGE = "해당사용자의 프로필을 찾을 수 없습니다.";

    public UserProfilePictureNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    // 커스텀 메시지를 받는 생성자도 추가
    public UserProfilePictureNotFoundException(String message) {
        super(message);
    }

    public UserProfilePictureNotFoundException(Exception cause) {
        super(DEFAULT_MESSAGE, cause);
    }

    public UserProfilePictureNotFoundException(String message, Exception cause) {
        super(message, cause);
    }

}
