package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "vulnerabilidad")
public class Vuln {
    public enum EstadoVulnerabilidad {
        PARCHADA,
        NO_PARCHADA
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "activo_afectado", nullable = false)
    private ActiveBase activoAfectado;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(length = 20)
    private String cve;

    private LocalDate fechaDeDescubrimiento;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EstadoVulnerabilidad estado;

    @ManyToMany
    @JoinTable(name = "vulnerabilidad_recomendacion", joinColumns = @JoinColumn(name = "vulnerabilidad_id"), inverseJoinColumns = @JoinColumn(name = "recomendacion_id"))
    private List<Recomendacion> recomendaciones;

    // Getters y Setters
}
