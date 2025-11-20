package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.ActiveFisic;
import java.util.List;

public interface ActivoFisicoRepository extends JpaRepository<ActiveFisic, Long> {

    List<ActiveFisic> findByNombreContainingIgnoreCase(String name);
}
