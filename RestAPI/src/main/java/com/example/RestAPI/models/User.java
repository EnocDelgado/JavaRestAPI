package com.example.RestAPI.models;

// Import necessary JPA and Lombok annotations
import jakarta.persistence.*;
import lombok.*;

@Getter   // Lombok annotation to generate the getter methods for all fields
@Setter   // Lombok annotation to generate the setter methods for all fields
@NoArgsConstructor // Lombok annotation to generate a no-args constructor
@AllArgsConstructor // Lombok annotation to generate a constructor with all arguments
@Entity  // Marks this class as a JPA entity (mapped to a database table)
@Table(name = "users")  // Specifies the name of the database table ("users") for this entity
public class User {

    @Id  // Marks this field as the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Automatically generates the ID with auto-increment
    private Long id;  // Unique identifier for the user

    @Column(unique = true)  // Ensures the username is unique in the database
    private String username;  // Username of the user

    private String password;  // Password of the user (usually stored as a hashed value)
}