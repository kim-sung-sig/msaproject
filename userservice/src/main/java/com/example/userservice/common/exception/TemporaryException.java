package com.example.userservice.common.exception;

public class TemporaryException extends RuntimeException {

    private final int retryAfterSeconds;

    public TemporaryException(int retryAfterSeconds) {
        super("일시적인 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
        this.retryAfterSeconds = retryAfterSeconds;
    }

    public int getRetryAfterSeconds() {
        return retryAfterSeconds;
    }

}
