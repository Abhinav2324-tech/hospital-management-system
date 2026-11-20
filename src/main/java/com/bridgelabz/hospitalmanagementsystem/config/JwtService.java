package com.bridgelabz.hospitalmanagementsystem.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.time.Instant;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Issues signed tokens and verifies them before returning the user's email.
 */
@Service
public class JwtService {

    private static final String ISSUER = "hospital-management-system";

    private final SecretKey signingKey;
    private final JwtParser parser;
    private final long expirationMillis;

    public JwtService(
            @Value("${jwt.secret}") String base64Secret,
            @Value("${jwt.expiration}") long expirationMillis) {
        this.signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(base64Secret));
        this.expirationMillis = expirationMillis;
        this.parser = Jwts.parser()
                .verifyWith(signingKey)
                .requireIssuer(ISSUER)
                .build();
    }

    public String generateToken(String email) {
        Instant now = Instant.now();

        return Jwts.builder()
                .issuer(ISSUER)
                .subject(email)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusMillis(expirationMillis)))
                .signWith(signingKey, Jwts.SIG.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        Claims claims = parser.parseSignedClaims(token).getPayload();
        String email = claims.getSubject();

        if (email == null || email.isBlank() || claims.getExpiration() == null) {
            throw new JwtException("Token is missing its subject or expiry");
        }

        return email;
    }
}