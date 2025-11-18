package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.ActiveBase;

public interface ActivoBaseRepository extends JpaRepository<ActiveBase, Long> {
}
