package com.example.navire.repository;

import com.example.navire.model.Voyage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoyageRepository extends JpaRepository<Voyage, Long> {
    boolean existsByNumBonLivraison(String numBonLivraison);
    boolean existsByNumTicket(String numTicket);

    // Allow checking existence of voyages by linked entities
    boolean existsByChauffeurId(Long chauffeurId);
    boolean existsByCamionId(Long camionId);

    java.util.List<Voyage> findByProjetId(Long projetId);
    java.util.List<Voyage> findByProjetClientId(Long projetClientId);
    
    // 🔥 Trouver un voyage par numéro de bon de livraison ET numéro de ticket
    java.util.List<Voyage> findByNumBonLivraisonAndNumTicket(String numBonLivraison, String numTicket);
}
