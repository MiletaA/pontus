package com.pontus.cargo.config;

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
 * OpenAPI configuration for Cargo Service
 * Handles cargo tracking, customs clearance, and dangerous goods management
 */
@Configuration
public class OpenApiConfig {

    @Value("${server.port:8083}")
    private String serverPort;

    @Bean
    public OpenAPI cargoServiceOpenAPI() {
        return new OpenAPI()
                .openapi("3.1.0")
                .info(new Info()
                        .title("Pontus Cargo Service API")
                        .description("Comprehensive cargo management service for Pontus Port Management System. " +
                                   "Handles cargo tracking, customs clearance, dangerous goods classification, " +
                                   "weight management, and cargo-vessel associations for efficient port operations.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Pontus Cargo Management Team")
                                .email("cargo@pontus.com")
                                .url("https://pontus.com/cargo"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT"))
                        .termsOfService("https://pontus.com/terms"))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:" + serverPort)
                                .description("Development Cargo Service"),
                        new Server()
                                .url("https://cargo.pontus.com")
                                .description("Production Cargo Service"),
                        new Server()
                                .url("http://localhost:8080/cargo")
                                .description("Via API Gateway (Development)"),
                        new Server()
                                .url("https://api.pontus.com/cargo")
                                .description("Via API Gateway (Production)")))
                .components(new Components()
                        .addSecuritySchemes("userInfo", new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .name("X-User-Info")
                                .description("User information header from API Gateway"))
                        .addSecuritySchemes("customsAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .name("X-Customs-Token")
                                .description("Customs authority authentication token")))
                .addSecurityItem(new SecurityRequirement().addList("userInfo"));
    }
}
