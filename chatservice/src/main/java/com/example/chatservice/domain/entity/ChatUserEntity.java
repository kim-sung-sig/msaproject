package com.example.chatservice.domain.entity;

import java.util.UUID;

import org.hibernate.annotations.Comment;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
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
    name = "dn_user_sync",
    indexes = {
        @Index(name = "idx_user_uuid", columnList = "uuid", unique = true)
    }
)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ChatUserEntity extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private UUID uuid;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    @Comment("사용자 권한")
    private UserRole role;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Comment("사용자 상태")
    private UserStatus status;

    private String nickName;

    private String profileImageUrl;

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

}
