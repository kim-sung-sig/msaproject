package com.example.chatservice.common.filter;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.chatservice.common.config.security.CustomUserDetails;
import com.example.chatservice.common.util.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        JwtUtil.getJwtFromRequest(request)
                .filter(JwtUtil::validateToken)
                .ifPresent(token -> {
                    CustomUserDetails customUser = new CustomUserDetails(token);
                    Authentication authToken = new UsernamePasswordAuthenticationToken(customUser, null, customUser.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                });

        // 필터 체인 진행
        filterChain.doFilter(request, response);
    }

}
