package com.example.navire.repository;

import com.example.navire.model.Dechargement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DechargementRepository extends JpaRepository<Dechargement, Long> {
    
    Optional<Dechargement> findByChargementId(Long chargementId);
    
    List<Dechargement> findByClientId(Long clientId);
    
    List<Dechargement> findByDepotId(Long depotId);
}
