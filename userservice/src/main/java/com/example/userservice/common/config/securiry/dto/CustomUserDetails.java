package com.example.userservice.common.config.securiry.dto;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.userservice.domain.entity.User.UserStatus;
import com.example.userservice.domain.model.UserForSecurity;

import lombok.Data;

@Data
public class CustomUserDetails implements UserDetails {

    private UserForSecurity user;

    public CustomUserDetails(UserForSecurity user){
        this.user = user;
    }

    @Override
    public String getUsername() {
        return user.username();
    }

    @Override
    public String getPassword() {
        return user.password();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(user.role().getKey()));
    }

    @Override
    public boolean isAccountNonLocked() {
        UserStatus status = user.status();
        boolean isLocked = Objects.equals(status, UserStatus.LOCKED);
        if(isLocked) { // 계정 잠김
            return false;
        }

        LocalDateTime lastLoginAt = user.lastLoginAt();
        if(lastLoginAt != null) {
            // 90일 기준으로 검사
            LocalDateTime now = LocalDateTime.now();
            return lastLoginAt.plusDays(90).isAfter(now); // 90일 이내
        }

        // 최초 로그인
        // 계정 생성일과 현재 날짜를 비교하여 90일 이내인지 확인
        LocalDateTime createdAt = user.createdAt();
        LocalDateTime now = LocalDateTime.now();
        return createdAt.plusDays(90).isAfter(now);
    }

    @Override
    public boolean isEnabled() { // 계정 만료 또는 삭제
        UserStatus lock = user.status();
        return Objects.equals(lock, UserStatus.ENABLED);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // 비밀번호 갱신 필요 여부 TODO 비밀번호 갱신 로직 추가
        return true;
    }

}
