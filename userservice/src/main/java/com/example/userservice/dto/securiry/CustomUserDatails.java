package com.example.userservice.dto.securiry;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.userservice.entity.User;
import com.example.userservice.enums.UserStatus;

import lombok.Data;

@Data
public class CustomUserDatails implements UserDetails {

    private User user;

    public CustomUserDatails(User user){
        this.user = user;
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(user.getRole().getKey()));
    }

    @Override
    public boolean isAccountNonLocked() {
        UserStatus status = user.getStatus();
        boolean isLocked = Objects.equals(status, UserStatus.LOCKED);
        if(isLocked) { // 계정 잠김
            return false;
        }

        LocalDateTime lastLoginAt = user.getLastLoginAt();
        if(lastLoginAt != null) {
            // 90일 기준으로 검사
            LocalDateTime now = LocalDateTime.now();
            return lastLoginAt.plusDays(90).isAfter(now); // 90일 이내
        }

        // 최초 로그인
        // 계정 생성일과 현재 날짜를 비교하여 90일 이내인지 확인
        LocalDateTime createdAt = user.getCreatedAt();
        LocalDateTime now = LocalDateTime.now();
        return createdAt.plusDays(90).isAfter(now);
    }

    @Override
    public boolean isEnabled() { // 계정 만료 또는 삭제
        UserStatus lock = user.getStatus();
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
