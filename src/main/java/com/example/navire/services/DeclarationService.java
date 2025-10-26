package com.example.navire.services;

import com.example.navire.dto.DeclarationDTO;
import com.example.navire.mapper.DeclarationMapper;
import com.example.navire.model.Declaration;
import com.example.navire.model.Projet;
import com.example.navire.repository.DeclarationRepository;
import com.example.navire.repository.ProjetRepository;
import com.example.navire.exception.ProjetNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeclarationService {

    @Autowired
    private DeclarationRepository declarationRepository;

    @Autowired
    private ProjetRepository projetRepository;

    @Autowired
    private DeclarationMapper declarationMapper;

    // Récupérer toutes les déclarations
    public List<DeclarationDTO> getAllDeclarations() {
        return declarationRepository.findAll().stream()
                .map(declarationMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Récupérer les déclarations d'un projet
    public List<DeclarationDTO> getDeclarationsByProjet(Long projetId) {
        return declarationRepository.findByProjetId(projetId).stream()
                .map(declarationMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Récupérer une déclaration par ID
    public DeclarationDTO getDeclarationById(Long id) {
        Declaration declaration = declarationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Déclaration non trouvée avec l'ID: " + id));
        return declarationMapper.toDTO(declaration);
    }

    // Créer une déclaration
    @Transactional
    public DeclarationDTO createDeclaration(DeclarationDTO declarationDTO) {
        // Vérifier que le projet existe
        Projet projet = projetRepository.findById(declarationDTO.getProjetId())
                .orElseThrow(() -> new ProjetNotFoundException(declarationDTO.getProjetId()));

        Declaration declaration = declarationMapper.toEntity(declarationDTO);
        declaration.setProjet(projet);
        
        Declaration saved = declarationRepository.save(declaration);
        return declarationMapper.toDTO(saved);
    }

    // Mettre à jour une déclaration
    @Transactional
    public DeclarationDTO updateDeclaration(Long id, DeclarationDTO declarationDTO) {
        Declaration declaration = declarationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Déclaration non trouvée avec l'ID: " + id));

        declaration.setNumeroDeclaration(declarationDTO.getNumeroDeclaration());
        declaration.setQuantiteManifestee(declarationDTO.getQuantiteManifestee());

        // Mettre à jour le projet si nécessaire
        if (declarationDTO.getProjetId() != null && !declarationDTO.getProjetId().equals(declaration.getProjet().getId())) {
            Projet projet = projetRepository.findById(declarationDTO.getProjetId())
                    .orElseThrow(() -> new ProjetNotFoundException(declarationDTO.getProjetId()));
            declaration.setProjet(projet);
        }

        Declaration updated = declarationRepository.save(declaration);
        return declarationMapper.toDTO(updated);
    }

    // Supprimer une déclaration
    @Transactional
    public void deleteDeclaration(Long id) {
        if (!declarationRepository.existsById(id)) {
            throw new RuntimeException("Déclaration non trouvée avec l'ID: " + id);
        }
        declarationRepository.deleteById(id);
    }
}
