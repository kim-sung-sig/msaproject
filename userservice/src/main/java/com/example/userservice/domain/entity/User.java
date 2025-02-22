package com.example.userservice.domain.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.userservice.domain.enums.UserRole;
import com.example.userservice.domain.enums.UserStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "dn_user",
    indexes = {
        @Index(name = "idx_user_uuid", columnList = "uuid", unique = true),
        @Index(name = "idx_user_username", columnList = "username", unique = true),
        @Index(name = "idx_user_nickname", columnList = "nick_name", unique = true),
        @Index(name = "idx_user_email", columnList = "email"),})
@EntityListeners(value = {AuditingEntityListener.class})
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    // key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid", nullable = false, unique = true)
    private UUID uuid;

    // security
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(name = "login_fail_count", columnDefinition = "int default 0")
    private int loginFailCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private UserStatus status;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @Column(name = "temp_password")
    private String tempPassword;

    @Column(name = "temp_password_expired_at")
    private LocalDateTime tempPasswordExpiredAt;


    // 사용자 정보
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "nick_name", nullable = false, unique = true)
    private String nickName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    // audit
    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public void changePassword(String password) {
        this.password = password;
    }

    public void changeTempPassword(String tempPassword, int minutes) {
        this.tempPassword = tempPassword;
        this.tempPasswordExpiredAt = LocalDateTime.now().plusMinutes(minutes);
    }

    public void increaseLoginFailCount() {
        this.loginFailCount++;
    }

    public void userDelete() {
        this.status = UserStatus.DELETED;
    }

    public void userLock() {
        this.status = UserStatus.LOCKED;
    }

    public void loginSuccess() {
        this.lastLoginAt = LocalDateTime.now();
        this.loginFailCount = 0;
    }
}
