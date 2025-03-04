package com.example.userservice.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "dn_nick_name_history")
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class NickNameHistory {

    @Id
    @Column(name = "nick_name")
    private String nickName;

    @Column(name = "seq")
    private Long seq;

    public void increaseSeq() {
        this.seq++;
    }

    @PrePersist
    protected void onPrePersist() {
        if (this.seq == null) {
            this.seq = 1L;
        }
    }

}
