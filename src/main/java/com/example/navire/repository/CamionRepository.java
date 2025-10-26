package com.example.navire.repository;

import com.example.navire.model.Camion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CamionRepository extends JpaRepository<Camion, Long> {
    boolean existsByMatricule(String matricule);
}
