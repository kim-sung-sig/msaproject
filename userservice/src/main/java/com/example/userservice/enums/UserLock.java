package com.example.userservice.enums;

public enum UserLock {

    ENABLED("계정 활성화"),
    LOCKED("계정 잠김"),
    EXPIRED("계정 만료"),
    DISABLED("계정 비활성화"),
    DELETED("계정 삭제");

    private final String title;

    private UserLock(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
