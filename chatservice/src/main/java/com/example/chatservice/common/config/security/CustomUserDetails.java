package com.example.chatservice.common.config.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.chatservice.common.util.JwtUtil;
import com.example.chatservice.domain.entity.ChatUserEntity.UserRole;

public class CustomUserDetails implements UserDetails {

    private final String username;
    private final String password;
    private final UserRole role;

    public CustomUserDetails(String token) {
        this.username = JwtUtil.getUsername(token);
        this.password = null;
        this.role = UserRole.valueOf(JwtUtil.getRole(token));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role.getKey()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

}
