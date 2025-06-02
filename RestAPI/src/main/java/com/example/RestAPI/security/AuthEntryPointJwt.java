package com.example.RestAPI.security;

import jakarta.servlet.http.HttpServletRequest;  // Import HttpServletRequest for handling incoming HTTP requests
import jakarta.servlet.http.HttpServletResponse; // Import HttpServletResponse for handling outgoing HTTP responses
import org.springframework.security.core.AuthenticationException; // Import AuthenticationException to handle authentication errors
import org.springframework.security.web.AuthenticationEntryPoint; // Import AuthenticationEntryPoint to customize the entry point for authentication
import org.springframework.stereotype.Component; // Import Component to define this class as a Spring-managed bean

import java.io.IOException; // Import IOException for handling I/O errors

@Component  // Marks this class as a Spring Bean that can be autowired and managed by the Spring container
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    // This method is called when an authentication error occurs (i.e., when the user is not authenticated).
    @Override
    public void commence(
            HttpServletRequest request,  // The incoming HTTP request
            HttpServletResponse response, // The outgoing HTTP response
            AuthenticationException authException // The exception thrown when authentication fails
    ) throws IOException {
        // Sends an HTTP 401 Unauthorized error with a custom error message in case of authentication failure
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Unauthorized");
    }
}
