package com.github.inventorysaleshub.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    // --- Password encoder ---
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // --- Authentication manager ---
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    // --- Security filter chain ---
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // Public endpoints
                .requestMatchers("/api/auth/**").permitAll()

                // Admin-only sections
                .requestMatchers("/api/dashboard/**").hasRole("ADMIN")
                .requestMatchers("/api/products/**").hasRole("ADMIN")
                .requestMatchers("/api/invoices/**", "/api/pay/**").hasRole("ADMIN")

                // Mixed access (USER and ADMIN)
                .requestMatchers("/api/orders/**").hasAnyRole("USER", "ADMIN")

                // Everything else requires authentication
                .anyRequest().authenticated()
            );

        return http.build();
    }

}
