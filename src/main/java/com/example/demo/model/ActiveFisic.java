package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "activosFisico")
public class ActiveFisic extends ActiveBase {

    public enum EstadoActivo {
        ACTIVO,
        MANTENIMIENTO,
        RETIRADO
    }

    @Column(length = 255)
    private String ubicacion;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EstadoActivo estado;

    // Getters y Setters
}
