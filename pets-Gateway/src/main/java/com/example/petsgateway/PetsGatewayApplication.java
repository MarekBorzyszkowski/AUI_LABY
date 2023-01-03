package com.example.petsgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@SpringBootApplication
public class PetsGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(PetsGatewayApplication.class, args);
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("breeds", b -> b.host("172.28.0.4:8080")
                        .and()
                        .path("/api/breeds", "/api/breeds/{id}")
                        .uri("http://172.28.0.6:8081"))
                .route("dogs", b -> b.host("172.28.0.4:8080")
                        .and()
                        .path("/api/dogs", "/api/dogs/{id}", "/api/breeds/{id}/dogs", "/api/breeds/{id}/dogs/{dId}")
                        .uri("http://172.28.0.5:8082"))
                .build();
    }

    @Bean
    public CorsWebFilter corsWebFilter(){
        final CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(Collections.singletonList("*"));
        corsConfig.setMaxAge(3600L);
        corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "PATCH"));
        corsConfig.addAllowedHeader("*");

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);

    }
}
