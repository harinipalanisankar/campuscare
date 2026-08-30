package com.campuscare.campuscare.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    // We added constraints here to ensure email is unique and never empty
    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    private String role; // Will store "ADMIN", "USER", or "WORKER"
}