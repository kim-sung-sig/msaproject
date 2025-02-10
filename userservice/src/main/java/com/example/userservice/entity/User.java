package com.example.userservice.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.userservice.enums.UserRole;
import com.example.userservice.enums.UserStatus;

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
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "dn_user",
    indexes = {
        @Index(name = "idx_user_uuid", columnList = "uuid", unique = true),
        @Index(name = "idx_user_username", columnList = "username", unique = true),
        @Index(name = "idx_user_nickname", columnList = "nick_name"),
        @Index(name = "idx_user_nick_seq", columnList = "nick_seq"),
        @Index(name = "idx_user_email", columnList = "email"),})
@EntityListeners(AuditingEntityListener.class)
@Data
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
    private Integer loginFailCount;
    @Enumerated(EnumType.STRING)
    private UserStatus status;
    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;


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

    // audit
    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    // mapping
    // @OneToMany(targetEntity = UserProfilePicture.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // private Set<UserProfilePicture> profiles;

    // @OneToMany(targetEntity = PasswordHistory.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // private Set<PasswordHistory> passwordHistories;

}
