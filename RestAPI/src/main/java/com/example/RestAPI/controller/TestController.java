package com.example.RestAPI.controller;

import org.springframework.web.bind.annotation.*;

@RestController  // Marks the class as a REST controller, capable of handling HTTP requests
@RequestMapping("/api/test")  // Base URL for this controller's endpoints
public class TestController {

    // Endpoint to access public content, accessible to all users
    @GetMapping("/all")  // HTTP GET request for "/api/test/all"
    public String allAccess() {
        return "Public Content.";  // Return a simple message indicating public content
    }

    // Endpoint to access content meant for user access, presumably after authentication
    @GetMapping("/user")  // HTTP GET request for "/api/test/user"
    public String userAccess() {
        return "User Content.";  // Return a simple message indicating user-specific content
    }
}