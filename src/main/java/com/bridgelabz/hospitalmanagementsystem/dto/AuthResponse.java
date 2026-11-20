package com.bridgelabz.hospitalmanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Returns the JWT and a message after successful login.
 */
@Getter
@AllArgsConstructor
public class AuthResponse {

    private final String token;
    private final String message;
}