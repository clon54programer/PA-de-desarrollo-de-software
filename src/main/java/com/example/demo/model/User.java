package com.example.demo.model;
import jakarta.persistence.*;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 80, nullable = false)
    private String name;

    @Column(length = 255, nullable = false) // almacenar hash de contraseña
    private String password;

    // Getters y Setters
}
