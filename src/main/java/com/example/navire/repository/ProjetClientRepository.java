package com.example.navire.repository;

import com.example.navire.model.ProjetClient;
import com.example.navire.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProjetClientRepository extends JpaRepository<ProjetClient, Long> {
    @Query("SELECT pc.client FROM ProjetClient pc WHERE pc.projet.id = :projetId")
    List<Client> findClientsByProjetId(@Param("projetId") Long projetId);

    List<ProjetClient> findByProjetId(Long projetId);
    
    List<ProjetClient> findByClientId(Long clientId);
}
