package com.example.userservice.common.config.securiry.dto;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.userservice.domain.entity.User.UserStatus;
import com.example.userservice.domain.model.UserForSecurity;

import lombok.Getter;

@Getter
public class CustomUserDetails implements UserDetails{

    private static final long serialVersionUID = 1L;

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
        return true;
    }

    @Override
    public boolean isEnabled() {
        UserStatus status = user.status();
        if (Objects.equals(status, UserStatus.LOCKED)) return false;
        return true;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

}
