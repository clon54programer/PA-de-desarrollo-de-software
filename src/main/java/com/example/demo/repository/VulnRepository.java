package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import com.example.demo.model.Vuln;
import com.example.demo.model.Vuln.EstadoVulnerabilidad;

public interface VulnRepository extends JpaRepository<Vuln, Long> {
    // Buscar vulnerabilidades por estado
    List<Vuln> findByEstado(EstadoVulnerabilidad estado);

    List<Vuln> findByActivoAfectadoId(@Param("activoId") Long activoId);

    // Buscar vulnerabilidades por CVE
    Vuln findByCve(String cve);
}
