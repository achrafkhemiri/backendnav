package com.example.navire.services;
import java.util.Set;
import java.util.HashSet;

import com.example.navire.dto.VoyageDTO;
import com.example.navire.exception.VoyageNotFoundException;
import com.example.navire.mapper.VoyageMapper;
import com.example.navire.model.*;
import com.example.navire.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VoyageService {
    public boolean projetExists(Long projetId) {
        return projetRepository.existsById(projetId);
    }
    public List<VoyageDTO> getVoyagesByProjetId(Long projetId) {
        return voyageRepository.findByProjetId(projetId)
            .stream()
            .map(voyageMapper::toDTO)
            .collect(java.util.stream.Collectors.toList());
    }
    @Autowired
    private VoyageRepository voyageRepository;
    @Autowired
    private VoyageMapper voyageMapper;
    @Autowired
    private ChauffeurRepository chauffeurRepository;
    @Autowired
    private CamionRepository camionRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private DepotRepository depotRepository;
    @Autowired
    private ProjetRepository projetRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private QuantiteService quantiteService;
    @Autowired
    private ProjetClientRepository projetClientRepository;

    public List<VoyageDTO> getAllVoyages() {
        return voyageRepository.findAll().stream()
                .map(voyageMapper::toDTO)
                .collect(Collectors.toList());
    }

    public VoyageDTO getVoyageById(Long id) {
        Voyage voyage = voyageRepository.findById(id)
                .orElseThrow(() -> new VoyageNotFoundException(id));
        return voyageMapper.toDTO(voyage);
    }

    @Transactional
    public VoyageDTO createVoyage(VoyageDTO dto) {
        if (voyageRepository.existsByNumBonLivraison(dto.getNumBonLivraison())) {
            throw new IllegalArgumentException("NumBonLivraison already exists");
        }
        if (voyageRepository.existsByNumTicket(dto.getNumTicket())) {
            throw new IllegalArgumentException("NumTicket already exists");
        }
        
        // Validation de la quantité si voyage vers client
        if (dto.getProjetClientId() != null && dto.getQuantite() != null && dto.getQuantite() > 0) {
            QuantiteService.ValidationResult validation = quantiteService.validerAjoutVoyage(
                dto.getProjetClientId(), 
                dto.getQuantite()
            );
            if (!validation.isValide()) {
                throw new IllegalArgumentException(validation.getMessage());
            }
        }
        
        Voyage voyage = new Voyage();
        voyage.setNumBonLivraison(dto.getNumBonLivraison());
        voyage.setNumTicket(dto.getNumTicket());
        voyage.setReste(dto.getReste());
        voyage.setDate(dto.getDate());
        voyage.setPoidsClient(dto.getPoidsClient());
        voyage.setPoidsDepot(dto.getPoidsDepot());
        voyage.setSociete(dto.getSociete());
    voyage.setSocieteP(dto.getSocieteP());
        voyage.setQuantite(dto.getQuantite() != null ? dto.getQuantite() : 0.0);
        // Ajout du chauffeur unique
        if (dto.getChauffeurId() != null) {
            Chauffeur chauffeur = chauffeurRepository.findById(dto.getChauffeurId()).orElse(null);
            voyage.setChauffeur(chauffeur);
        } else {
            voyage.setChauffeur(null);
        }
        // Ajout du camion unique
        if (dto.getCamionId() != null) {
            Camion camion = camionRepository.findById(dto.getCamionId()).orElse(null);
            voyage.setCamion(camion);
        } else {
            voyage.setCamion(null);
        }
        voyage.setClient(dto.getClientId() != null ? clientRepository.findById(dto.getClientId()).orElse(null) : null);
        voyage.setDepot(dto.getDepotId() != null ? depotRepository.findById(dto.getDepotId()).orElse(null) : null);
        voyage.setProjet(dto.getProjetId() != null ? projetRepository.findById(dto.getProjetId()).orElse(null) : null);
        voyage.setProjetClient(dto.getProjetClientId() != null ? projetClientRepository.findById(dto.getProjetClientId()).orElse(null) : null);
    // Autorisation code (si fourni)
    voyage.setAutorisationCode(dto.getAutorisationCode());
        // Ajout de l'utilisateur unique
        if (dto.getUserId() != null) {
            User user = userRepository.findById(dto.getUserId()).orElse(null);
            voyage.setUser(user);
        } else {
            voyage.setUser(null);
        }
        return voyageMapper.toDTO(voyageRepository.save(voyage));
    }

    @Transactional
    public VoyageDTO updateVoyage(Long id, VoyageDTO dto) {
        Voyage voyage = voyageRepository.findById(id)
                .orElseThrow(() -> new VoyageNotFoundException(id));
        
        // Validation quantité si modification et voyage vers client
        if (dto.getProjetClientId() != null && dto.getQuantite() != null && dto.getQuantite() > 0) {
            // Calculer la différence avec l'ancienne quantité
            double ancienneQuantite = voyage.getQuantite() != null ? voyage.getQuantite() : 0.0;
            double difference = dto.getQuantite() - ancienneQuantite;
            
            if (difference > 0) {
                QuantiteService.ValidationResult validation = quantiteService.validerAjoutVoyage(
                    dto.getProjetClientId(), 
                    difference
                );
                if (!validation.isValide()) {
                    throw new IllegalArgumentException(validation.getMessage());
                }
            }
        }
        
        voyage.setNumBonLivraison(dto.getNumBonLivraison());
        voyage.setNumTicket(dto.getNumTicket());
        voyage.setReste(dto.getReste());
        voyage.setDate(dto.getDate());
        voyage.setPoidsClient(dto.getPoidsClient());
        voyage.setPoidsDepot(dto.getPoidsDepot());
        voyage.setSociete(dto.getSociete());
    voyage.setSocieteP(dto.getSocieteP());
        voyage.setQuantite(dto.getQuantite() != null ? dto.getQuantite() : voyage.getQuantite());
        // Ajout du chauffeur unique
        if (dto.getChauffeurId() != null) {
            Chauffeur chauffeur = chauffeurRepository.findById(dto.getChauffeurId()).orElse(null);
            voyage.setChauffeur(chauffeur);
        } else {
            voyage.setChauffeur(null);
        }
        // Ajout du camion unique
        if (dto.getCamionId() != null) {
            Camion camion = camionRepository.findById(dto.getCamionId()).orElse(null);
            voyage.setCamion(camion);
        } else {
            voyage.setCamion(null);
        }
        voyage.setClient(dto.getClientId() != null ? clientRepository.findById(dto.getClientId()).orElse(null) : null);
        voyage.setDepot(dto.getDepotId() != null ? depotRepository.findById(dto.getDepotId()).orElse(null) : null);
        voyage.setProjet(dto.getProjetId() != null ? projetRepository.findById(dto.getProjetId()).orElse(null) : null);
        voyage.setProjetClient(dto.getProjetClientId() != null ? projetClientRepository.findById(dto.getProjetClientId()).orElse(null) : null);
    // Autorisation code (si fourni)
    voyage.setAutorisationCode(dto.getAutorisationCode());
        // Ajout de l'utilisateur unique
        if (dto.getUserId() != null) {
            User user = userRepository.findById(dto.getUserId()).orElse(null);
            voyage.setUser(user);
        } else {
            voyage.setUser(null);
        }
        return voyageMapper.toDTO(voyageRepository.save(voyage));
    }

    @Transactional
    public void deleteVoyage(Long id) {
        if (!voyageRepository.existsById(id)) {
            throw new VoyageNotFoundException(id);
        }
        voyageRepository.deleteById(id);
    }
}
