package com.example.navire.repository;

import com.example.navire.model.Chauffeur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChauffeurRepository extends JpaRepository<Chauffeur, Long> {
	Chauffeur findByNumCin(String numCin);
}
