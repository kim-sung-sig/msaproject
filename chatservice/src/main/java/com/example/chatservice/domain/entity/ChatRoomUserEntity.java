package com.example.chatservice.domain.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "dn_chat_room_user")
public class ChatRoomUserEntity extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", updatable = false, nullable = false)
    private Long userId;

    @Column(name = "chat_room_id", updatable = false, nullable = false)
    private Long chatRoomId;

    @Column(name = "chat_room_name", nullable = false)
    private String chat_room_name;

    // TODO 알림설정 음소거 등등

}
