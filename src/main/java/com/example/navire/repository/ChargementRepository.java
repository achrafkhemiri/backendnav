package com.example.navire.repository;

import com.example.navire.model.Chargement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChargementRepository extends JpaRepository<Chargement, Long> {
    
    List<Chargement> findByCamionId(Long camionId);
    
    List<Chargement> findByChauffeurId(Long chauffeurId);
    
    List<Chargement> findByProjetId(Long projetId);
    
    List<Chargement> findBySociete(String societe);
}
