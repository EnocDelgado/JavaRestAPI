package com.example.RestAPI.repository;

// Import necessary Spring Data JPA and model classes
import com.example.RestAPI.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository  // Marks this interface as a Spring Data JPA repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Custom method to find a user by their username
    User findByUsername(String username);

    // Custom method to check if a username already exists in the database
    boolean existsByUsername(String username);
}