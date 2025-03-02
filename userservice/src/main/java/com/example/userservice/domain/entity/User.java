package com.example.userservice.domain.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.userservice.common.enums.EventType;
import com.example.userservice.common.util.EventPublisher;
import com.example.userservice.domain.event.user.UserEvent;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(
    name = "dn_user",
    indexes = {
        @Index(name = "idx_user_uuid", columnList = "uuid", unique = true),
        @Index(name = "idx_user_username", columnList = "username", unique = true),
        @Index(name = "idx_user_nickname", columnList = "nick_name", unique = true),
        @Index(name = "idx_user_email", columnList = "email"),})
@EntityListeners(value = {AuditingEntityListener.class})
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@ToString
@EqualsAndHashCode
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    // key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid", nullable = false, unique = true)
    @ColumnTransformer(write = "uuid_to_bin(?)", read = "bin_to_uuid(?)")
    private UUID uuid;

    // security
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    @Comment("사용자 권한")
    private UserRole role;

    @Column(name = "login_fail_count", columnDefinition = "int default 0")
    @Comment("로그인 실패 횟수")
    private int loginFailCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Comment("사용자 상태")
    private UserStatus status;

    @Column(name = "last_login_at")
    @Comment("마지막 로그인 일시")
    private LocalDateTime lastLoginAt;

    @Column(name = "temp_password")
    @Comment("임시 비밀번호")
    private String tempPassword;

    @Column(name = "temp_password_expired_at")
    @Comment("임시 비밀번호 만료 일시")
    private LocalDateTime tempPasswordExpiredAt;


    // 사용자 정보
    @Column(name = "name", nullable = false)
    @Comment("사용자 이름")
    private String name;

    @Column(name = "nick_name", nullable = false, unique = true)
    @Comment("사용자 닉네임")
    private String nickName;

    @Column(name = "email")
    @Comment("사용자 이메일")
    private String email;

    @Column(name = "phone")
    @Comment("사용자 전화번호")
    private String phone;

    // audit
    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    // mapping
    @OneToMany(targetEntity = UserProfilePicture.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<UserProfilePicture> userProfilePictures;

    @OneToMany(targetEntity = PasswordHistory.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<PasswordHistory> passwordHistories;

    // method

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setTempPassword(String tempPassword, int minutes) {
        this.tempPassword = tempPassword;
        this.tempPasswordExpiredAt = LocalDateTime.now().plusMinutes(minutes);
    }

    public void incrementLoginFailureCount() {
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

    public enum UserRole {
        USER("ROLE_USER", "사용자"),
        ADMIN("ROLE_ADMIN", "관리자");

        private final String key;
        private final String title;

        private UserRole(String key, String title) {
            this.key = key;
            this.title = title;
        }

        public String getKey() {
            return key;
        }

        public String getTitle() {
            return title;
        }
    }

    public enum UserStatus {
        ENABLED("계정 활성화"),
        LOCKED("계정 잠김"),
        EXPIRED("계정 만료"),
        DISABLED("계정 비활성화"),
        DELETED("계정 삭제");

        private final String title;

        private UserStatus(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }
    }

    @PrePersist
    public void _prePersist() {
        // 도메인 모델 생성시 필수 값 체크로직?
        if (this.uuid == null) {
            this.uuid = UUID.randomUUID();
        }
    }

    @PostPersist
    public void _postPersist() {
        EventPublisher.publish(new UserEvent(EventPublisher.class, this, EventType.CREATED));
    }

    @PreUpdate
    public void _preUpdate() {
        // 도메인 모델에 관한 필수값 체크로직?
    }

    @PostUpdate
    public void _postUpdate() {
        EventPublisher.publish(new UserEvent(EventPublisher.class, this, EventType.UPDATED));
    }

    @PreRemove
    public void _preDestroy() {

    }

    @PostRemove
    public void _postDestroy() {
        EventPublisher.publish(new UserEvent(EventPublisher.class, this, EventType.DELETED));
    }

}
