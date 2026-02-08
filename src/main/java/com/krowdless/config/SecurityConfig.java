package com.krowdless.config;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	private final JwtAuthFilter jwtAuthFilter;

	public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
		this.jwtAuthFilter = jwtAuthFilter;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http
				// CORS
				.cors(cors -> cors.configurationSource(corsConfigurationSource()))

				// Disable CSRF for stateless APIs
				.csrf(AbstractHttpConfigurer::disable)

				// Stateless session
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

				// Authorization rules
				.authorizeHttpRequests(auth -> auth

						// Preflight requests
						.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

						.requestMatchers("/api/v1/auth/**", "/actuator/**").permitAll()
						
                        .requestMatchers("/api/v1/user/**").authenticated()

						.anyRequest().permitAll())

				// Add JWT filter
				.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

				// Exception handling (delegates to your handlers)
	            .exceptionHandling(ex -> ex
	                .authenticationEntryPoint((request, response, authException) -> {
	                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
	                })
	                .accessDeniedHandler((request, response, accessDeniedException) -> {
	                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
	                })
	            );

		return http.build();
	}

	// CORS configuration
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();

		config.setAllowedOriginPatterns(
				List.of("http://localhost:*", "https://*.railway.app", "https://*.onrender.com"));

		config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));

		config.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept"));

		config.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

		source.registerCorsConfiguration("/**", config);
		return source;
	}

	// Password encoder
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
