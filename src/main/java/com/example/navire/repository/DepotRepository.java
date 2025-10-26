package com.example.navire.repository;

import com.example.navire.model.Depot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepotRepository extends JpaRepository<Depot, Long> {
    boolean existsByNom(String nom);
}
