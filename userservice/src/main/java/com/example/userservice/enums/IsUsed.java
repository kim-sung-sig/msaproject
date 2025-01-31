package com.example.userservice.enums;

public enum IsUsed {

    ENABLED("사용"),
    DISABLED("미사용"),
    DELETED("삭제");

    private String name;

    private IsUsed(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
