package com.example.RestAPI.controller;

import com.example.RestAPI.models.User;
import com.example.RestAPI.repository.UserRepository;
import com.example.RestAPI.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth") // Define the base URL for authentication routes
public class AuthController {

    // Inject the AuthenticationManager to authenticate users
    @Autowired
    AuthenticationManager authenticationManager;

    // Inject the UserRepository to interact with the database (for user registration)
    @Autowired
    UserRepository userRepository;

    // Inject the PasswordEncoder to encode user passwords before saving
    @Autowired
    PasswordEncoder encoder;

    // Inject the JwtUtil utility class to generate JWT tokens after successful authentication
    @Autowired
    JwtUtil jwtUtils;

    // Endpoint to authenticate users (sign-in)
    @PostMapping("/signin")
    public String authenticateUser(@RequestBody User user) {
        // Perform the authentication using the AuthenticationManager
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(), // Get the username from the request body
                        user.getPassword()  // Get the password from the request body
                )
        );

        // Retrieve user details from the authentication object
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // Generate and return a JWT token based on the authenticated user's username
        return jwtUtils.generateToken(userDetails.getUsername());
    }

    // Endpoint to register a new user (sign-up)
    @PostMapping("/signup")
    public String registerUser(@RequestBody User user) {
        // Check if the username already exists in the database
        if (userRepository.existsByUsername(user.getUsername())) {
            return "Error: Username is already taken!"; // Return error message if username exists
        }

        // Create a new User object, encoding the password before saving
        User newUser = new User(
                null, // Auto-generated ID by the database
                user.getUsername(), // Use the username from the request body
                encoder.encode(user.getPassword()) // Encode the password before saving it to the database
        );

        // Save the new user to the database
        userRepository.save(newUser);

        // Return a success message after registration
        return "User registered successfully!";
    }
}
