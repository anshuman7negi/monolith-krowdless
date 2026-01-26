package com.krowdless.usersmangement.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {

        @Autowired
        private JwtAuthFilter jwtAuthFilter;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http)
                        throws Exception {

                http
                                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                                .csrf(csrf -> csrf.disable())

                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                                .authorizeHttpRequests(auth -> auth

                                                // ✅ PRE-FLIGHT
                                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                                                // ✅ PUBLIC ENDPOINTS
                                                .requestMatchers(
                                                                "/users/login",
                                                                "/users/register",
                                                                "/users/refresh",
                                                                "/users/ping",
                                                                "/actuator/**",
                                                                "/master/**",

                                                                // ✅ PUBLIC DESTINATION APIs
                                                                "/api/destinations",
                                                                "/api/destinations/**",
                                                                "/api/stays",
                                                                "/api/stays/**")
                                                .permitAll()

                                                // 🔐 USER SELF APIs (JWT REQUIRED)
                                                .requestMatchers("/users/me/**").authenticated()

                                                // 🔒 EVERYTHING ELSE
                                                .anyRequest().authenticated());

                http.addFilterBefore(
                                jwtAuthFilter,
                                UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }

        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration config = new CorsConfiguration();

                // ✅ FRONTEND ORIGINS
                config.setAllowedOriginPatterns(List.of(
                                "http://localhost:*",
                                "https://*.railway.app",
                                "https://krowdless.onrender.com",
                                "https://demo-krowdless.onrender.com"));

                config.setAllowedMethods(List.of(
                                "GET", "POST", "PUT", "DELETE", "OPTIONS"));

                config.setAllowedHeaders(List.of(
                                "Authorization",
                                "Content-Type"));

                config.setAllowCredentials(true);

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", config);

                return source;
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }
}
