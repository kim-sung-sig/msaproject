package com.example.userservice.domain.entity;

import java.util.UUID;

import org.hibernate.annotations.Comment;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.userservice.common.enums.IsUsed;

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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "dn_user_profile_picture",
    indexes = {
        @Index(name = "idx_user_profile_picture_uuid", columnList = "uuid", unique = true),
        @Index(name = "idx_user_profile_picture_user_id", columnList = "user_id"),
        @Index(name = "idx_user_profile_picture_status", columnList = "status"),})
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Builder
@Getter
public class UserProfilePicture extends BaseEntity {

    // key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid")
    private UUID uuid;

    @ManyToOne(targetEntity = User.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private IsUsed status;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_type")
    private String fileType;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "file_path")
    @Comment("파일 저장 경로")
    private String filePath;

    @Column(name = "file_extension")
    private String fileExtension;

    @Column(name = "file_url")
    private String fileUrl;

    @PrePersist
    protected void onPrePersist() {
        super.onPrePersist();

    }

    @PostPersist
    protected void onPostPersist() {
    }

    @PreUpdate
    protected void onPreUpdate() {
        super.onPreUpdate();
    }

    @PostUpdate
    protected void onPostUpdate() {

    }

}
