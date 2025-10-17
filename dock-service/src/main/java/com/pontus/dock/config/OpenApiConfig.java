package com.pontus.dock.config;

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
 * OpenAPI configuration for Dock Service
 * Handles dock allocation, berth management, and vessel-dock assignments
 */
@Configuration
public class OpenApiConfig {

    @Value("${server.port:8082}")
    private String serverPort;

    @Bean
    public OpenAPI dockServiceOpenAPI() {
        return new OpenAPI()
                .openapi("3.1.0")
                .info(new Info()
                        .title("Pontus Dock Service API")
                        .description("Comprehensive dock management service for Pontus Port Management System. " +
                                   "Handles dock allocation, berth scheduling, vessel assignments, dangerous goods handling, " +
                                   "and port infrastructure management for efficient maritime operations.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Pontus Dock Management Team")
                                .email("docks@pontus.com")
                                .url("https://pontus.com/docks"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT"))
                        .termsOfService("https://pontus.com/terms"))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:" + serverPort)
                                .description("Development Dock Service"),
                        new Server()
                                .url("https://docks.pontus.com")
                                .description("Production Dock Service"),
                        new Server()
                                .url("http://localhost:8080/docks")
                                .description("Via API Gateway (Development)"),
                        new Server()
                                .url("https://api.pontus.com/docks")
                                .description("Via API Gateway (Production)")))
                .components(new Components()
                        .addSecuritySchemes("userInfo", new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .name("X-User-Info")
                                .description("User information header from API Gateway"))
                        .addSecuritySchemes("harborAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .name("X-Harbor-Master")
                                .description("Harbor master authorization token")))
                .addSecurityItem(new SecurityRequirement().addList("userInfo"));
    }
}
