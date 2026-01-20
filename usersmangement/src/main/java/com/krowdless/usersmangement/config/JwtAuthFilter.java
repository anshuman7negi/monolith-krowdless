package com.krowdless.usersmangement.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.krowdless.usersmangement.entity.UserEntity;
import com.krowdless.usersmangement.repository.UserRepository;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getServletPath();

        // ✅ PUBLIC & PRE-FLIGHT BYPASS
        if (
            path.startsWith("/users/")
            || path.startsWith("/actuator/")
            || "OPTIONS".equalsIgnoreCase(request.getMethod())
        ) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");

        // ✅ NO AUTH HEADER → SKIP JWT
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.substring(7);

        // ✅ EMPTY TOKEN SAFETY
        if (jwt.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String username = jwtUtil.extractUsername(jwt);

            if (username != null &&
                SecurityContextHolder.getContext().getAuthentication() == null) {

                UserEntity user = userRepository
                        .findByUsername(username)
                        .orElse(null);

                if (user != null && jwtUtil.validateToken(jwt, username)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    user,
                                    null,
                                    new ArrayList<>()
                            );
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request)
                    );

                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            // ❗ NEVER BLOCK REQUEST
            filterChain.doFilter(request, response);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
