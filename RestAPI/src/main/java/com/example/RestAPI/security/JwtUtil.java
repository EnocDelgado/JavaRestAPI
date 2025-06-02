package com.example.RestAPI.security;

import io.jsonwebtoken.*;  // Import necessary classes from the JJWT library for creating and parsing JWT tokens
import io.jsonwebtoken.security.Keys;  // Import Keys for generating cryptographic keys for signing the JWT
import jakarta.annotation.PostConstruct;  // Import PostConstruct for initializing the key after the bean is constructed
import org.springframework.beans.factory.annotation.Value;  // Import Value to inject configuration properties
import org.springframework.stereotype.Component;  // Import Component to register this class as a Spring-managed bean
import javax.crypto.SecretKey;  // Import SecretKey for handling the cryptographic key used to sign JWT tokens
import java.nio.charset.StandardCharsets;  // Import StandardCharsets for encoding the JWT secret in UTF-8
import java.util.Date;  // Import Date for handling token expiration times

@Component  // Marks this class as a Spring-managed bean for automatic dependency injection
public class JwtUtil {

    // Inject JWT secret key and expiration time from application.properties or application.yml
    @Value("${jwt.secret}")
    private String jwtSecret;  // The secret key used for signing the JWT
    @Value("${jwt.expiration}")
    private int jwtExpirationMs;  // Expiration time for the JWT in milliseconds

    private SecretKey key;  // The SecretKey used to sign and verify the JWT token

    // This method is invoked after the bean is constructed and all dependencies are injected.
    // It initializes the 'key' using the provided jwtSecret.
    @PostConstruct
    public void init() {
        // Converts the jwtSecret string into a SecretKey for HMACSHA256 signing algorithm.
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    // Method to generate a JWT token using a given username.
    public String generateToken(String username) {
        // Builds a JWT token with the provided username and other claims like issue date and expiration time.
        return Jwts.builder()
                .setSubject(username)  // Set the subject (the username) of the token
                .setIssuedAt(new Date())  // Set the token's issue date to the current date and time
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))  // Set the token's expiration date
                .signWith(key, SignatureAlgorithm.HS256)  // Sign the token using the secret key and HS256 algorithm
                .compact();  // Build and return the compact token string
    }

    // Method to extract the username from a JWT token.
    public String getUsernameFromToken(String token) {
        // Parses the JWT token, validates it, and extracts the subject (username).
        return Jwts.parserBuilder()
                .setSigningKey(key)  // Use the secret key to validate the token
                .build()
                .parseClaimsJws(token)  // Parse the JWT and get the claims (i.e., payload) from the token
                .getBody()
                .getSubject();  // Return the subject (username) of the token
    }

    // Method to validate the JWT token.
    public boolean validateJwtToken(String token) {
        try {
            // Tries to parse the token using the provided secret key. If the token is valid, it will not throw exceptions.
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;  // If parsing is successful, the token is valid.
        } catch (SecurityException e) {
            System.out.println("Invalid JWT signature: " + e.getMessage());  // If the signature is invalid
        } catch (MalformedJwtException e) {
            System.out.println("Invalid JWT token: " + e.getMessage());  // If the token is malformed
        } catch (ExpiredJwtException e) {
            System.out.println("JWT token is expired: " + e.getMessage());  // If the token is expired
        } catch (UnsupportedJwtException e) {
            System.out.println("JWT token is unsupported: " + e.getMessage());  // If the token's format is unsupported
        } catch (IllegalArgumentException e) {
            System.out.println("JWT claims string is empty: " + e.getMessage());  // If the claims string is empty
        }
        return false;  // Return false if the token is invalid or an exception occurs
    }
}
