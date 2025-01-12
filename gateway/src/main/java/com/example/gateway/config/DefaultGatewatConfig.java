package com.example.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@Configuration
public class DefaultGatewatConfig implements Ordered {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE) // 가장 높은 우선순위
    RouteLocator defaultRoute(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("DEFAULT_ROUTE", r -> r
                        .path("/**")
                        .filters(f -> f.setStatus(404))
                        .uri("no://op"))
                .build();
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

}
