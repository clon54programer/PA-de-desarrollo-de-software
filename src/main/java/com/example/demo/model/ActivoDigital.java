package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "activosDigital")
public class ActivoDigital extends ActiveBase {

    @Column(length = 255)
    private String licencia;

    @Column(length = 20)
    private String version;

    public ActivoDigital(long id, String name, String descripcion, Double valor, String licencia, String version) {
        super(id, name, descripcion, valor);
        this.licencia = licencia;
        this.version = version;
    }

    // Getters y Setters

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getLicencia() {
        return licencia;
    }

    public void setLicencia(String licencia) {
        this.licencia = licencia;
    }

}
