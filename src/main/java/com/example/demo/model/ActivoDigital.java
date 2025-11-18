package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "activosDigital")
public class ActivoDigital extends ActiveBase {

    @Column(length = 255)
    private String licencia;

    @Column(length = 20)
    private String version;

    // Getters y Setters
}
