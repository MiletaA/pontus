package com.pontus.crew.config;

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
 * OpenAPI configuration for Crew Service
 * Handles crew member management, certifications, and maritime personnel records
 */
@Configuration
public class OpenApiConfig {

    @Value("${server.port:8084}")
    private String serverPort;

    @Bean
    public OpenAPI crewServiceOpenAPI() {
        return new OpenAPI()
                .openapi("3.1.0")
                .info(new Info()
                        .title("Pontus Crew Service API")
                        .description("Comprehensive crew management service for Pontus Port Management System. " +
                                   "Handles crew member registration, certification tracking, passport management, " +
                                   "rank assignments, and maritime personnel records for vessel operations.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Pontus Crew Management Team")
                                .email("crew@pontus.com")
                                .url("https://pontus.com/crew"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT"))
                        .termsOfService("https://pontus.com/terms"))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:" + serverPort)
                                .description("Development Crew Service"),
                        new Server()
                                .url("https://crew.pontus.com")
                                .description("Production Crew Service"),
                        new Server()
                                .url("http://localhost:8080/crew")
                                .description("Via API Gateway (Development)"),
                        new Server()
                                .url("https://api.pontus.com/crew")
                                .description("Via API Gateway (Production)")))
                .components(new Components()
                        .addSecuritySchemes("userInfo", new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .name("X-User-Info")
                                .description("User information header from API Gateway"))
                        .addSecuritySchemes("maritimeAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .name("X-Maritime-Authority")
                                .description("Maritime authority certification token")))
                .addSecurityItem(new SecurityRequirement().addList("userInfo"));
    }
}
