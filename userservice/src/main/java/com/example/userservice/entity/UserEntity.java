package com.example.userservice.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.example.userservice.enums.UserLock;
import com.example.userservice.enums.UserRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ms_user")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class UserEntity {

    // key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private UUID uuid;

    // security
    @Column(name = "username", nullable = false, unique = true)
    private String username;
    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    // 사용자 정보
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "nick_name", nullable = false)
    private String nickName;

    @Column(name = "nick_seq", nullable = false)
    private Long nickSeq;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    // 계정 상태
    private UserLock lock;

    // audit
    private LocalDateTime lastLoginAt;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public UserEntity(UUID uuid, String username, String password, UserRole role, String name, String nickName, Long nickSeq, String email, String phone, UserLock lock) {
        this.uuid = uuid;
        this.username = username;
        this.password = password;
        this.role = role;
        this.name = name;
        this.nickName = nickName;
        this.nickSeq = nickSeq;
        this.email = email;
        this.phone = phone;
        this.lock = lock;
    }

    public void updateUser(String name, String nickName, Long nickSeq, String email, String phone) {
        this.name = name;
        this.nickName = nickName;
        this.nickSeq = nickSeq;
        this.email = email;
        this.phone = phone;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateRole(UserRole role) {
        this.role = role;
    }

    public void updateLock(UserLock lock) {
        this.lock = lock;
    }

}
