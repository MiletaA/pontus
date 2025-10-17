package com.pontus.auth.config;

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
 * OpenAPI configuration for Authentication Service
 * Handles user authentication, authorization, and JWT token management
 */
@Configuration
public class OpenApiConfig {

    @Value("${server.port:8086}")
    private String serverPort;

    @Bean
    public OpenAPI pontusAuthOpenAPI() {
        return new OpenAPI()
                .openapi("3.1.0")
                .info(new Info()
                        .title("Pontus Authentication Service API")
                        .description("Comprehensive authentication and authorization service for Pontus Port Management System. " +
                                   "Handles user registration, login, JWT token management, role-based access control, " +
                                   "and security for all maritime operations including harbor masters, customs officers, " +
                                   "dock workers, vessel captains, and port managers.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Pontus Security Team")
                                .email("security@pontus.com")
                                .url("https://pontus.com/security"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT"))
                        .termsOfService("https://pontus.com/terms"))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:" + serverPort)
                                .description("Development Authentication Service"),
                        new Server()
                                .url("https://auth.pontus.com")
                                .description("Production Authentication Service"),
                        new Server()
                                .url("http://localhost:8080/auth")
                                .description("Via API Gateway (Development)"),
                        new Server()
                                .url("https://api.pontus.com/auth")
                                .description("Via API Gateway (Production)")))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("JWT Bearer token for authenticated requests"))
                        .addSecuritySchemes("refreshToken", new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .name("X-Refresh-Token")
                                .description("Refresh token for obtaining new access tokens")))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }
}
