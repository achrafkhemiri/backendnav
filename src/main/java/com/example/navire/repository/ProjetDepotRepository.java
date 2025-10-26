package com.example.navire.repository;

import com.example.navire.model.ProjetDepot;
import com.example.navire.model.Depot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProjetDepotRepository extends JpaRepository<ProjetDepot, Long> {
    @Query("SELECT pd.depot FROM ProjetDepot pd WHERE pd.projet.id = :projetId")
    List<Depot> findDepotsByProjetId(@Param("projetId") Long projetId);

    List<ProjetDepot> findByProjetId(Long projetId);
    
    List<ProjetDepot> findByDepotId(Long depotId);
    
    Optional<ProjetDepot> findByProjetIdAndDepotId(Long projetId, Long depotId);
}
