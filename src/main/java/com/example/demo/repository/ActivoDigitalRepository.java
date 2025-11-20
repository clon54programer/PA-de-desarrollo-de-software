package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.ActivoDigital;

public interface ActivoDigitalRepository extends JpaRepository<ActivoDigital, Long> {
    List<ActivoDigital> findByNameContainingIgnoreCase(String name);
}