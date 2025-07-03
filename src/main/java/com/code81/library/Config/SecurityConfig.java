package com.code81.library.Config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                        // Book, Author, Publisher - LIBRARIAN & ADMINISTRATOR
                        .requestMatchers("/api/books/**", "/api/authors/**", "/api/publishers/**")
                        .hasAnyRole("ADMINISTRATOR", "LIBRARIAN")

                        // Delete operations - ADMINISTRATOR only
                        .requestMatchers("/api/users/delete/**", "/api/books/delete/**")
                        .hasRole("ADMINISTRATOR")

                        // User Management - ADMINISTRATOR only
                        .requestMatchers("/api/users/**")
                        .hasRole("ADMINISTRATOR")

                        // Borrowers: create/update by LIBRARIAN, view by STAFF & ADMINISTRATOR
                        .requestMatchers("/api/borrowers/create", "/api/borrowers/update/**")
                        .hasRole("LIBRARIAN")
                        .requestMatchers("/api/borrowers/**")
                        .hasAnyRole("LIBRARIAN", "STAFF", "ADMINISTRATOR")

                        // Transactions
                        .requestMatchers("/api/transactions/borrow")
                        .hasAnyRole("LIBRARIAN", "ADMINISTRATOR")
                        .requestMatchers("/api/transactions/return")
                        .hasAnyRole("LIBRARIAN", "ADMINISTRATOR", "STAFF")
                        .requestMatchers("/api/transactions/**")
                        .hasAnyRole("LIBRARIAN", "ADMINISTRATOR")

                        // Read-only access (e.g. catalog/search/categories)
                        .requestMatchers("/api/catalog/**", "/api/search/**", "/api/categories/**")
                        .hasAnyRole("STAFF", "LIBRARIAN", "ADMINISTRATOR")

                        // All other endpoints require authentication
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults()); // Use HTTP Basic Authentication

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
