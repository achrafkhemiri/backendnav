package com.example.navire.repository;

import com.example.navire.model.Voyage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoyageRepository extends JpaRepository<Voyage, Long> {
    boolean existsByNumBonLivraison(String numBonLivraison);
    boolean existsByNumTicket(String numTicket);

    java.util.List<Voyage> findByProjetId(Long projetId);
    java.util.List<Voyage> findByProjetClientId(Long projetClientId);
}
