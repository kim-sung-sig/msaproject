package com.example.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class GatewayConfig {

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

}
