package com.example.RestAPI.security;

import com.example.RestAPI.service.CustomUserDetailsService; // Import custom service for loading user details
import jakarta.servlet.FilterChain;  // Import FilterChain for processing the request filter
import jakarta.servlet.ServletException; // Import ServletException for handling servlet-related errors
import jakarta.servlet.http.HttpServletRequest; // Import HttpServletRequest for processing incoming HTTP requests
import jakarta.servlet.http.HttpServletResponse; // Import HttpServletResponse for handling outgoing HTTP responses
import org.springframework.beans.factory.annotation.Autowired; // Import Autowired for dependency injection
import org.springframework.security.core.userdetails.UserDetails; // Import UserDetails to represent authenticated user details
import org.springframework.security.core.context.SecurityContextHolder; // Import SecurityContextHolder to manage authentication context
import org.springframework.security.authentication.*; // Import authentication classes (e.g., UsernamePasswordAuthenticationToken)
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource; // Import for setting authentication details (e.g., remote address, session ID)
import org.springframework.stereotype.Component; // Import Component to register this class as a Spring-managed bean
import org.springframework.web.filter.OncePerRequestFilter; // Import OncePerRequestFilter to ensure the filter runs only once per request

import java.io.IOException; // Import IOException for handling input/output exceptions

@Component  // Marks this class as a Spring Bean for automatic management and injection by Spring
public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtils; // Autowired dependency for utility class to handle JWT operations (e.g., parsing, validating)

    @Autowired
    private CustomUserDetailsService userDetailsService; // Autowired service to load user details by username

    // Overriding the doFilterInternal method from OncePerRequestFilter to filter the HTTP request
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,  // Incoming HTTP request
            HttpServletResponse response, // Outgoing HTTP response
            FilterChain filterChain  // Chain of filters to pass the request through after this one
    ) throws ServletException, IOException {
        try {
            // Extract the JWT token from the HTTP request
            String jwt = parseJwt(request);

            // If the token is valid, proceed with authentication
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {

                // Extract the username from the token
                String username = jwtUtils.getUsernameFromToken(jwt);

                // Load the user details from the database using the username
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // Create an authentication token using the user details (no password required for JWT-based authentication)
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,  // The user details loaded from the database
                                null,  // No password needed since we are authenticating with the JWT
                                userDetails.getAuthorities()  // The authorities/roles granted to the user
                        );

                // Set the authentication details (e.g., request metadata) on the authentication object
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Set the authentication in the SecurityContextHolder to mark the user as authenticated
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            // Handle any errors during authentication (e.g., invalid token)
            System.out.println("Cannot set user authentication: " + e);
        }

        // Continue with the next filter in the chain (if any)
        filterChain.doFilter(request, response);
    }

    // Method to extract the JWT token from the "Authorization" header of the HTTP request
    private String parseJwt(HttpServletRequest request) {
        // Get the "Authorization" header from the HTTP request
        String headerAuth = request.getHeader("Authorization");

        // Check if the header contains a token starting with "Bearer " and extract the token part
        if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
            // Remove the "Bearer " prefix and return the actual token
            return headerAuth.substring(7);
        }

        // Return null if the token is not found or the header is malformed
        return null;
    }
}
