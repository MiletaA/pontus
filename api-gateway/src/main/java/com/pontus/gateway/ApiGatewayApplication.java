package com.pontus.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.http.HttpMethod;

import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;

@SpringBootApplication
@EnableWebFluxSecurity
public class ApiGatewayApplication {

    // Same JWT secret as used in auth service and vessel service
    @Value("${JWT_SECRET}")
    private String jwtSecret;

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/auth/**").permitAll() // Auth endpoints are public
                                .pathMatchers(HttpMethod.OPTIONS, "/**").permitAll() // Allow OPTIONS requests for CORS preflight
                        .anyExchange().authenticated() // All other requests require authentication
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtDecoder(jwtDecoder())) // Use JWT for authentication
                )
                .csrf(csrf -> csrf.disable()) // Disable CSRF for REST APIs
                .cors(cors -> cors.disable()) // Disable Spring Security CORS - handled by Spring Cloud Gateway global CORS
                .build();
    }

    @Bean
    public ReactiveJwtDecoder jwtDecoder() {
        // Create JWT decoder with the same secret key used by auth service
        // Use Keys.hmacShaKeyFor to match the auth service's key generation
        SecretKey secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        return NimbusReactiveJwtDecoder.withSecretKey(secretKey)
                .macAlgorithm(MacAlgorithm.HS256)  
                .build();
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // Auth Service Routes (Public)
                .route("auth_route", r -> r.path("/auth/**")
                        .uri("http://auth-service:8086"))

                // Vessel Service Routes (Protected)
                .route("vessel_route_root", r -> r.path("/vessels")
                        .filters(f -> f.rewritePath("/vessels", "/api/vessels"))
                        .uri("http://vessel-service:8081"))
                .route("vessel_route_sub", r -> r.path("/vessels/**")
                        .filters(f -> f.rewritePath("/vessels/(?<segment>.*)", "/api/vessels/${segment}"))
                        .uri("http://vessel-service:8081"))

                // Dock Service Routes (Protected)
                .route("dock_route_root", r -> r.path("/docks")
                        .filters(f -> f.rewritePath("/docks", "/api/docks"))
                        .uri("http://dock-service:8082"))
                .route("dock_route_sub", r -> r.path("/docks/**")
                        .filters(f -> f.rewritePath("/docks/(?<segment>.*)", "/api/docks/${segment}"))
                        .uri("http://dock-service:8082"))

                // Cargo Service Routes (Protected)
                .route("cargo_route_root", r -> r.path("/cargo")
                        .filters(f -> f.rewritePath("/cargo", "/api/cargo"))
                        .uri("http://cargo-service:8083"))
                .route("cargo_route_sub", r -> r.path("/cargo/**")
                        .filters(f -> f.rewritePath("/cargo/(?<segment>.*)", "/api/cargo/${segment}"))
                        .uri("http://cargo-service:8083"))

                // Crew Service Routes (Protected)
                .route("crew_route_root", r -> r.path("/crew")
                        .filters(f -> f.rewritePath("/crew", "/api/crew"))
                        .uri("http://crew-service:8084"))
                .route("crew_route_sub", r -> r.path("/crew/**")
                        .filters(f -> f.rewritePath("/crew/(?<segment>.*)", "/api/crew/${segment}"))
                        .uri("http://crew-service:8084"))

                // Delivery Service Routes (Protected)
                .route("delivery_route_root", r -> r.path("/deliveries")
                        .filters(f -> f.rewritePath("/deliveries", "/api/deliveries"))
                        .uri("http://delivery-service:8085"))
                .route("delivery_route_sub", r -> r.path("/deliveries/**")
                        .filters(f -> f.rewritePath("/deliveries/(?<segment>.*)", "/api/deliveries/${segment}"))
                        .uri("http://delivery-service:8085"))

                .build();
    }
}
