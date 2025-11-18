package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.example.demo.model.Vuln;
import com.example.demo.model.Vuln.EstadoVulnerabilidad;

public interface VulnRepository extends JpaRepository<Vuln, Long> {
    // Buscar vulnerabilidades por estado
    List<Vuln> findByEstado(EstadoVulnerabilidad estado);

    // Buscar vulnerabilidades por CVE
    Vuln findByCve(String cve);
}
