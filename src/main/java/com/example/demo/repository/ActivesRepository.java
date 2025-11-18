package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.ActiveBase;
import com.example.demo.model.ActiveFisic;
import com.example.demo.model.ActivoDigital;

public interface ActivoBaseRepository extends JpaRepository<ActiveBase, Long> {
}

public interface ActivoFisicoRepository extends JpaRepository<ActiveFisic, Long> {
}

public interface ActivoDigitalRepository extends JpaRepository<ActivoDigital, Long> {
}
