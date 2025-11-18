package com.example.demo.model;

import java.math.BigDecimal;

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
    private BigDecimal valor;

    public ActiveBase(long id, String name, String descripcion, BigDecimal valor) {
        this.id = id;
        this.name = name;
        this.descripcion = descripcion;
        this.valor = valor;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

}
