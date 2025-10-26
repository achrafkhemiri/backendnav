package com.example.navire.repository;

import com.example.navire.model.Declaration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeclarationRepository extends JpaRepository<Declaration, Long> {
    
    // Trouver toutes les déclarations d'un projet
    List<Declaration> findByProjetId(Long projetId);
    
    // Trouver par numéro de déclaration
    Declaration findByNumeroDeclaration(String numeroDeclaration);
}
