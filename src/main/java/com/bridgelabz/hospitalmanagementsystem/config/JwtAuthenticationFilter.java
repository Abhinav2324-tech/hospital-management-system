package com.bridgelabz.hospitalmanagementsystem.config;

import com.bridgelabz.hospitalmanagementsystem.repository.UserRepository;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Authenticates requests that contain a valid Bearer token for an existing user.
 */
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header == null || !header.regionMatches(true, 0, "Bearer ", 0, 7)
                || SecurityContextHolder.getContext().getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }

        String email;
        try {
            email = jwtService.extractUsername(header.substring(7).trim());
        } catch (JwtException | IllegalArgumentException ex) {
            // Leave the request unauthenticated. Protected routes will return 401.
            filterChain.doFilter(request, response);
            return;
        }

        userRepository.findByEmail(email).ifPresent(user -> {
            var authority = new SimpleGrantedAuthority("ROLE_" + user.getRole().name());
            var authentication = UsernamePasswordAuthenticationToken.authenticated(
                    user.getEmail(), null, List.of(authority));

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);
        });

        filterChain.doFilter(request, response);
    }
}