package com.example.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.gateway.filter.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class GatewayConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter; // 각 서비스가 JWT 인즈 필터가 필요한 경우 선 검사를 위해 사용

    @Bean
    RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("USERSERVICE", r -> r
                        .path("/ms1/**")
                        // .filters(f -> f.filter(jwtAuthenticationFilter))
                        .uri("lb://USERSERVICE"))

                // 기본 라우팅 설정
                .route("DEFAULT_ROUTE", r -> r
                        .path("/**") // 매칭되지 않는 모든 경로 처리
                        .filters(f -> f.setStatus(404)) // 404 응답 반환
                        .uri("no://op")) // 빈 URI (실제 요청을 전달하지 않음)
                .build();
    }
}
