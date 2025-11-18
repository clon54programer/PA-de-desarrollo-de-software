package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "recomendacion")
public class Recomendacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    // Getters y Setters
}
