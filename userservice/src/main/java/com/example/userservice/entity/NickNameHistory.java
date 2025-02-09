package com.example.userservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "dn_nick_name_history")
@Data
public class NickNameHistory {

    @Id
    @Column(name = "nick_name")
    private String nickName;

    @Column(name = "seq", columnDefinition = "int default 1")
    private Long seq;

}
