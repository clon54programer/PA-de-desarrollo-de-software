package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.Recomendacion;

public interface RecomendacionRepository extends JpaRepository<Recomendacion, Long> {
}
