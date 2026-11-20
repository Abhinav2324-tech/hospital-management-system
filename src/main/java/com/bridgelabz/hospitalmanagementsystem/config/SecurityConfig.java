package com.bridgelabz.hospitalmanagementsystem.config;

import com.bridgelabz.hospitalmanagementsystem.repository.UserRepository;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Enables stateless JWT authentication for the patient API.
 */
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http, JwtService jwtService, UserRepository userRepository)
            throws Exception {

        JwtAuthenticationFilter jwtFilter =
                new JwtAuthenticationFilter(jwtService, userRepository);

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
                        .requestMatchers(HttpMethod.POST,
                                "/api/auth/register", "/api/auth/login").permitAll()
                        .requestMatchers("/api/patients", "/api/patients/**").authenticated()
                        .anyRequest().denyAll())
                .exceptionHandling(errors -> errors.authenticationEntryPoint(
                        (request, response, exception) ->
                                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED)))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}