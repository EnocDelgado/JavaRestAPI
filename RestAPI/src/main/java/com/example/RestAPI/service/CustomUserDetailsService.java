package com.example.RestAPI.service;

import com.example.RestAPI.models.User;  // Import the User model
import com.example.RestAPI.repository.UserRepository;  // Import the UserRepository interface to access the user data
import org.springframework.beans.factory.annotation.Autowired;  // Import the annotation for dependency injection
import org.springframework.security.core.userdetails.*;  // Import the UserDetails and UserDetailsService interfaces for user authentication
import org.springframework.stereotype.Service;  // Import the annotation to mark this class as a service
import java.util.Collections;  // Import the Collections utility class for creating an empty list

// The @Service annotation marks this class as a Spring service, which can be injected into other components
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;  // Autowire the UserRepository to fetch user data from the database

    // Override the loadUserByUsername method, which is required by Spring Security's UserDetailsService interface
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Attempt to find the user by username in the database
        User user = userRepository.findByUsername(username);

        // If the user is not found, throw a UsernameNotFoundException
        if (user == null) {
            throw new UsernameNotFoundException("User Not Found with username: " + username);
        }

        // If the user is found, return a Spring Security UserDetails object with the user's username and password
        // The third argument is the authorities (roles) of the user, here it's an empty list as no roles are defined in this example
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),  // Username from the User entity
                user.getPassword(),  // Password from the User entity
                Collections.emptyList()  // Empty list of authorities (no roles are included here)
        );
    }
}
