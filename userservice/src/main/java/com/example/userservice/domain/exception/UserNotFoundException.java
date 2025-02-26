package com.example.userservice.domain.exception;

import jakarta.persistence.EntityNotFoundException;

public class UserNotFoundException extends EntityNotFoundException {

    private static final String DEFAULT_MESSAGE = "사용자를 찾을 수 없습니다.";

    public UserNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    // 커스텀 메시지를 받는 생성자도 추가
    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(Exception cause) {
        super(DEFAULT_MESSAGE, cause);
    }

    public UserNotFoundException(String message, Exception cause) {
        super(message, cause);
    }

}
