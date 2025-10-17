package com.pontus.vessel.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

/**
 * OpenAPI configuration for Vessel Service
 * Provides comprehensive API documentation with security schemes and server configurations
 */
@Configuration
public class OpenApiConfig {

    @Value("${server.port:8081}")
    private String serverPort;

    @Bean
    public OpenAPI vesselServiceOpenAPI() {
        return new OpenAPI()
                .openapi("3.1.0")
                .info(new Info()
                        .title("Vessel Service API")
                        .version("1.0.0")
                        .description("Comprehensive vessel management API for the Pontus port management system. " +
                                   "Handles vessel registration, tracking, scheduling, and status management. " +
                                   "Supports operations for container ships, bulk carriers, tankers, and other maritime vessels.")
                        .contact(new Contact()
                                .name("Pontus Vessel Management Team")
                                .email("vessels@pontus.com")
                                .url("https://pontus.com/vessels"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT"))
                        .termsOfService("https://pontus.com/terms"))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:" + serverPort)
                                .description("Development Vessel Service"),
                        new Server()
                                .url("https://vessels.pontus.com")
                                .description("Production Vessel Service"),
                        new Server()
                                .url("http://localhost:8080/vessels")
                                .description("Via API Gateway (Development)"),
                        new Server()
                                .url("https://api.pontus.com/vessels")
                                .description("Via API Gateway (Production)")))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("JWT Bearer token for authentication"))
                        .addSecuritySchemes("userInfo", new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .name("X-User-Info")
                                .description("User information header from API Gateway")))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .addSecurityItem(new SecurityRequirement().addList("userInfo"));
    }
}
