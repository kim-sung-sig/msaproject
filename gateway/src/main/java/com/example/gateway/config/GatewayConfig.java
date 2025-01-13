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

                .route("DEFAULT_ROUTE", r -> r
                        .path("/**")
                        .filters(f -> f.setStatus(404))
                        .uri("no://op"))
                .build();
    }

    // @Bean
    // @Order(Ordered.HIGHEST_PRECEDENCE) // 가장 낮은 우선순위
    // RouteLocator defaultRoute(RouteLocatorBuilder builder) {
    //     return builder.routes()
    //             .route("DEFAULT_ROUTE", r -> r
    //                     .path("/**")
    //                     .filters(f -> f.setStatus(404))
    //                     .uri("no://op"))
    //             .build();
    // }

}
