package com.bridgelabz.hospitalmanagementsystem.service;

import com.bridgelabz.hospitalmanagementsystem.dto.AuthRequest;
import com.bridgelabz.hospitalmanagementsystem.entity.Role;
import com.bridgelabz.hospitalmanagementsystem.entity.User;
import com.bridgelabz.hospitalmanagementsystem.repository.UserRepository;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.bridgelabz.hospitalmanagementsystem.config.JwtService;
import com.bridgelabz.hospitalmanagementsystem.dto.AuthResponse;
/**
 * Registers user accounts and verifies login credentials.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void registerUser(AuthRequest request) {
        String name = request.getName() == null ? "" : request.getName().strip();

        if (name.isEmpty() || name.length() > 255) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Name must contain 1 to 255 characters");
        }

        if (!isValidPasswordLength(request.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Password must contain at least 8 characters and at most 72 UTF-8 bytes");
        }

        String email = normalizeEmail(request.getEmail());

        if (userRepository.existsByEmail(email)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Email is already registered");
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);
    }

    public AuthResponse loginUser(AuthRequest request) {
        String email = authenticate(request);
        String token = jwtService.generateToken(email);
        return new AuthResponse(token, "Login successful");
    }
    public String authenticate(AuthRequest request) {
        if (!isValidPasswordLength(request.getPassword())) {
            throw invalidCredentials();
        }

        String email = normalizeEmail(request.getEmail());
        User user = userRepository.findByEmail(email)
                .orElseThrow(this::invalidCredentials);

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw invalidCredentials();
        }

        return user.getEmail();
    }

    private String normalizeEmail(String email) {
        return email.strip().toLowerCase(Locale.ROOT);
    }

    private boolean isValidPasswordLength(String password) {
        return password != null
                && password.length() >= 8
                && password.getBytes(StandardCharsets.UTF_8).length <= 72;
    }

    private ResponseStatusException invalidCredentials() {
        return new ResponseStatusException(
                HttpStatus.UNAUTHORIZED, "Invalid email or password");
    }
}