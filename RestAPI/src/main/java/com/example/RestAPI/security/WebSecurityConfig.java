package com.example.RestAPI.security;

import com.example.RestAPI.service.CustomUserDetailsService;  // Import the custom service that loads user details
import org.springframework.beans.factory.annotation.Autowired;  // Import the annotation for dependency injection
import org.springframework.context.annotation.Bean;  // Import the annotation to define beans in Spring context
import org.springframework.context.annotation.Configuration;  // Import the annotation to mark the class as a configuration class
import org.springframework.security.authentication.AuthenticationManager;  // Import the AuthenticationManager interface for authenticating users
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;  // Import the class for configuring authentication
import org.springframework.security.config.annotation.web.builders.HttpSecurity;  // Import the HttpSecurity class for defining HTTP security configurations
import org.springframework.security.config.http.SessionCreationPolicy;  // Import the SessionCreationPolicy enum for session handling
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;  // Import the BCryptPasswordEncoder for password hashing
import org.springframework.security.crypto.password.PasswordEncoder;  // Import PasswordEncoder interface for encoding passwords
import org.springframework.security.web.SecurityFilterChain;  // Import the SecurityFilterChain interface for defining filter chains
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;  // Import the filter for handling user authentication

@Configuration  // Marks this class as a configuration class for Spring
public class WebSecurityConfig {

    @Autowired
    CustomUserDetailsService userDetailsService;  // Autowire the custom service to load user details

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;  // Autowire the entry point handler for unauthorized access

    // Bean to create and provide the AuthTokenFilter, which handles JWT token validation
    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();  // Instantiate and return the AuthTokenFilter
    }

    // Bean to create and provide the AuthenticationManager, which handles authentication
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();  // Return the configured AuthenticationManager
    }

    // Bean to define the PasswordEncoder, used for encoding and verifying passwords
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // Use BCrypt hashing algorithm for password encoding
    }

    // Bean to configure and provide the SecurityFilterChain, which defines the HTTP security for the application
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // Updated configuration for Spring Security 6.x
        http
                .csrf(csrf -> csrf.disable())  // Disable CSRF (Cross-Site Request Forgery) protection. This is common when using stateless authentication (JWT)
                .cors(cors -> cors.disable())  // Disable CORS (Cross-Origin Resource Sharing). Enable if needed for handling cross-origin requests
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.authenticationEntryPoint(unauthorizedHandler)  // Handle authentication exceptions with the custom unauthorized handler
                )
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // Set session management to stateless, meaning no session will be created or used by Spring Security
                )
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/api/auth/**", "/api/test/all").permitAll()  // Permit access to authentication-related and public test endpoints
                                .anyRequest().authenticated()  // All other requests require authentication
                );

        // Add the JWT Token filter before the UsernamePasswordAuthenticationFilter to intercept the HTTP request and validate the JWT token
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();  // Return the configured SecurityFilterChain
    }
}
