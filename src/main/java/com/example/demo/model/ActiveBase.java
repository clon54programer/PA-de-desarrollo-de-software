package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "activosBase")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class ActiveBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 80, nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(precision = 15, scale = 2, nullable = false)
    private Double valor;

    // Getters y Setters
}
