package com.example.demo.model;

import java.math.BigDecimal;

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

    public ActiveFisic(long id, String name, String descripcion, BigDecimal valor, EstadoActivo estatus,
            String ubicacion) {
        super(id, name, descripcion, valor);
        this.estado = estatus;
        this.ubicacion = ubicacion;
    }

    // Getters y Setters
    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public EstadoActivo getEstado() {
        return estado;
    }

    public void setEstado(EstadoActivo estado) {
        this.estado = estado;
    }

}
