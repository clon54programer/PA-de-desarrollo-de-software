package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.ActiveBase;
import java.util.List;

public interface ActivoBaseRepository extends JpaRepository<ActiveBase, Long> {
    List<ActiveBase> findByNombreContainingIgnoreCase(String name);

}
