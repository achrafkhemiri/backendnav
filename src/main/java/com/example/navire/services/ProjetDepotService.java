package com.example.navire.services;

import com.example.navire.dto.ProjetDepotDTO;
import com.example.navire.exception.DepotNotFoundException;
import com.example.navire.exception.ProjetNotFoundException;
import com.example.navire.exception.QuantiteDepassementException;
import com.example.navire.mapper.ProjetDepotMapper;
import com.example.navire.model.ProjetDepot;
import com.example.navire.model.Projet;
import com.example.navire.model.Depot;
import com.example.navire.repository.ProjetDepotRepository;
import com.example.navire.repository.ProjetRepository;
import com.example.navire.repository.DepotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjetDepotService {
    @Autowired
    private ProjetDepotRepository projetDepotRepository;
    @Autowired
    private ProjetDepotMapper projetDepotMapper;
    @Autowired
    private ProjetRepository projetRepository;
    @Autowired
    private DepotRepository depotRepository;
    @Autowired
    private QuantiteService quantiteService;

    public List<ProjetDepotDTO> getAllProjetDepots() {
        return projetDepotRepository.findAll().stream()
                .map(projetDepotMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<ProjetDepotDTO> getProjetDepotsByProjetId(Long projetId) {
        return projetDepotRepository.findByProjetId(projetId).stream()
                .map(projetDepotMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<ProjetDepotDTO> getProjetDepotsByDepotId(Long depotId) {
        return projetDepotRepository.findByDepotId(depotId).stream()
                .map(projetDepotMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ProjetDepotDTO getProjetDepotById(Long id) {
        ProjetDepot projetDepot = projetDepotRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ProjetDepot not found with id: " + id));
        return projetDepotMapper.toDTO(projetDepot);
    }

    @Transactional
    public ProjetDepotDTO createProjetDepot(ProjetDepotDTO projetDepotDTO) {
        // Valider que le projet existe
        Projet projet = projetRepository.findById(projetDepotDTO.getProjetId())
                .orElseThrow(() -> new ProjetNotFoundException(projetDepotDTO.getProjetId()));
        
        // Valider que le depot existe
        Depot depot = depotRepository.findById(projetDepotDTO.getDepotId())
                .orElseThrow(() -> new DepotNotFoundException(projetDepotDTO.getDepotId()));
        
        // Valider la quantité avant d'ajouter
        QuantiteService.ValidationResult validation = quantiteService.validerAjoutDepot(
            projetDepotDTO.getProjetId(), 
            projetDepotDTO.getQuantiteAutorisee()
        );
        
        if (!validation.isValide()) {
            throw new RuntimeException(validation.getMessage());
        }
        
        // Créer le ProjetDepot
        ProjetDepot projetDepot = new ProjetDepot();
        projetDepot.setProjet(projet);
        projetDepot.setDepot(depot);
        projetDepot.setQuantiteAutorisee(projetDepotDTO.getQuantiteAutorisee());
        
        projetDepotRepository.save(projetDepot);
        return projetDepotMapper.toDTO(projetDepot);
    }

    @Transactional
    public ProjetDepotDTO updateQuantiteAutorisee(Long projetDepotId, Double quantiteAutorisee) {
        ProjetDepot projetDepot = projetDepotRepository.findById(projetDepotId)
                .orElseThrow(() -> new RuntimeException("ProjetDepot not found: " + projetDepotId));
        
        // Calculer la différence de quantité (positive = augmentation, négative = diminution)
        double ancienneQuantite = projetDepot.getQuantiteAutorisee() != null ? projetDepot.getQuantiteAutorisee() : 0.0;
        double difference = quantiteAutorisee - ancienneQuantite;
        
        // Si on augmente la quantité, valider qu'il y a assez de quantité disponible dans le projet
        // En tenant compte que l'ancienne quantité sera libérée
        if (difference > 0) {
            QuantiteService.ValidationResult validation = quantiteService.validerAjoutDepot(
                projetDepot.getProjet().getId(), 
                difference  // Seulement la différence (augmentation nette)
            );
            
            if (!validation.isValide()) {
                throw new QuantiteDepassementException(validation.getMessage());
            }
        }
        // Si on diminue ou garde la même quantité, pas besoin de validation (on libère de l'espace)
        
        projetDepot.setQuantiteAutorisee(quantiteAutorisee);
        projetDepotRepository.save(projetDepot);
        return projetDepotMapper.toDTO(projetDepot);
    }

    @Transactional
    public void deleteProjetDepot(Long id) {
        if (!projetDepotRepository.existsById(id)) {
            throw new RuntimeException("ProjetDepot not found with id: " + id);
        }
        projetDepotRepository.deleteById(id);
    }
}
