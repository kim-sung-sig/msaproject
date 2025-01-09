package com.example.chatservice.routers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class TestRouter {

    @Bean
    RouterFunction<ServerResponse> testRouterFunction() {
        return RouterFunctions.route(RequestPredicates.GET("/ms2"), request -> {
            String param = request.queryParam("param").orElse("default");
            return ServerResponse.ok().bodyValue("Hello from ms1: " + param);
        });
    }
}
