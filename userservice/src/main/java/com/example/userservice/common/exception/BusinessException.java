package com.example.userservice.common.exception;

import java.util.Collections;
import java.util.Map;

import org.springframework.lang.NonNull;

public class BusinessException extends RuntimeException {

    private Map<String, Object> errors;

    // 기본 생성자 (단순 메시지만 전달할 경우)
    public BusinessException(String message) {
        super(message);
        this.errors = Collections.emptyMap();
    }

    // 단일 필드 예외 생성자
    public BusinessException(String message, String field) {
        super(message);
        this.errors = Collections.singletonMap(field, message);
    }

    // 여러 필드 예외를 받을 수 있도록 추가
    public BusinessException(String message, @NonNull Map<String, String> errors) {
        super(message);
        this.errors = Collections.unmodifiableMap(errors);
    }

    public Map<String, Object> getErrors() {
        return errors;
    }

}
