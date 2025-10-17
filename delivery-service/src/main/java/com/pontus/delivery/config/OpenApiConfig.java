package com.pontus.delivery.config;

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
 * OpenAPI configuration for Delivery Service
 * Handles delivery scheduling, logistics coordination, and cargo distribution
 */
@Configuration
public class OpenApiConfig {

    @Value("${server.port:8085}")
    private String serverPort;

    @Bean
    public OpenAPI deliveryServiceOpenAPI() {
        return new OpenAPI()
                .openapi("3.1.0")
                .info(new Info()
                        .title("Pontus Delivery Service API")
                        .description("Comprehensive delivery management service for Pontus Port Management System. " +
                                   "Handles delivery scheduling, logistics coordination, cargo distribution, " +
                                   "transportation planning, and last-mile delivery operations for port cargo.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Pontus Delivery Management Team")
                                .email("deliveries@pontus.com")
                                .url("https://pontus.com/deliveries"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT"))
                        .termsOfService("https://pontus.com/terms"))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:" + serverPort)
                                .description("Development Delivery Service"),
                        new Server()
                                .url("https://deliveries.pontus.com")
                                .description("Production Delivery Service"),
                        new Server()
                                .url("http://localhost:8080/deliveries")
                                .description("Via API Gateway (Development)"),
                        new Server()
                                .url("https://api.pontus.com/deliveries")
                                .description("Via API Gateway (Production)")))
                .components(new Components()
                        .addSecuritySchemes("userInfo", new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .name("X-User-Info")
                                .description("User information header from API Gateway"))
                        .addSecuritySchemes("logisticsAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .name("X-Logistics-Token")
                                .description("Logistics provider authentication token")))
                .addSecurityItem(new SecurityRequirement().addList("userInfo"));
    }
}
