package com.pontus.auth.config;

import com.pontus.auth.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    static {
        System.out.println("========== SecurityConfig CLASS LOADING - STATIC BLOCK ==========");
        System.err.println("========== SecurityConfig CLASS LOADING - STATIC BLOCK ==========");
    }

    public SecurityConfig() {
        System.out.println("========== AUTH SecurityConfig CONSTRUCTOR CALLED ==========");
        System.err.println("========== AUTH SecurityConfig CONSTRUCTOR CALLED ==========");
    }

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        System.out.println("========== AUTH SecurityConfig - DISABLING ALL SECURITY ==========");
        System.err.println("========== AUTH SecurityConfig - DISABLING ALL SECURITY ==========");
        
        // Completely disable Spring Security to avoid any interference
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.disable())
            .authorizeHttpRequests(authz -> authz.anyRequest().permitAll())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .securityContext(context -> context.disable())
            .anonymous(anonymous -> anonymous.disable());

        System.out.println("========== AUTH SecurityConfig - ALL SECURITY DISABLED ==========");
        System.err.println("========== AUTH SecurityConfig - ALL SECURITY DISABLED ==========");
        return http.build();
    }


}
