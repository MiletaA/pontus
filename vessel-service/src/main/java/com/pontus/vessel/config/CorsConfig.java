package com.pontus.vessel.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                // Explicitly disable CORS - all CORS handling is done by API Gateway
                // This prevents Spring Boot from auto-configuring CORS with wildcard '*'
                registry.addMapping("/**").allowedOrigins(); // Empty origins = no CORS headers
            }
        };
    }
}
