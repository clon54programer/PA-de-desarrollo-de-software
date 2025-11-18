package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

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

    public Vuln(Long id, ActiveBase activoAfectado, String descripcion, String cve, EstadoVulnerabilidad estado,
            LocalDate fechaDeDescubrimiento) {
        this.activoAfectado = activoAfectado;
        this.id = id;
        this.descripcion = descripcion;
        this.cve = cve;
        this.fechaDeDescubrimiento = fechaDeDescubrimiento;
        this.recomendaciones = new ArrayList<>();

    }

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ActiveBase getActivoAfectado() {
        return activoAfectado;
    }

    public void setActivoAfectado(ActiveBase activoAfectado) {
        this.activoAfectado = activoAfectado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCve() {
        return cve;
    }

    public void setCve(String cve) {
        this.cve = cve;
    }

    public LocalDate getFechaDeDescubrimiento() {
        return fechaDeDescubrimiento;
    }

    public void setFechaDeDescubrimiento(LocalDate fechaDeDescubrimiento) {
        this.fechaDeDescubrimiento = fechaDeDescubrimiento;
    }

}
