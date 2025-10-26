package com.example.navire.services;

import com.example.navire.dto.ChargementDTO;
import com.example.navire.exception.*;
import com.example.navire.mapper.ChargementMapper;
import com.example.navire.model.*;
import com.example.navire.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChargementService {

    private final ChargementRepository chargementRepository;
    private final ChargementMapper chargementMapper;
    private final CamionRepository camionRepository;
    private final ChauffeurRepository chauffeurRepository;
    private final ProjetRepository projetRepository;

    @Transactional(readOnly = true)
    public List<ChargementDTO> getAllChargements() {
        return chargementRepository.findAll()
                .stream()
                .map(chargementMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ChargementDTO getChargementById(Long id) {
        Chargement chargement = chargementRepository.findById(id)
                .orElseThrow(() -> new ChargementNotFoundException(id));
        return chargementMapper.toDTO(chargement);
    }

    @Transactional
    public ChargementDTO createChargement(ChargementDTO chargementDTO) {
        // Valider l'existence des entités liées
        Camion camion = camionRepository.findById(chargementDTO.getCamionId())
                .orElseThrow(() -> new CamionNotFoundException(chargementDTO.getCamionId()));
        
        Chauffeur chauffeur = chauffeurRepository.findById(chargementDTO.getChauffeurId())
                .orElseThrow(() -> new ChauffeurNotFoundException(chargementDTO.getChauffeurId()));
        
        Projet projet = projetRepository.findById(chargementDTO.getProjetId())
                .orElseThrow(() -> new ProjetNotFoundException(chargementDTO.getProjetId()));

        Chargement chargement = new Chargement();
        chargement.setCamion(camion);
        chargement.setChauffeur(chauffeur);
        chargement.setSociete(chargementDTO.getSociete());
    chargement.setSocieteP(chargementDTO.getSocieteP());
        chargement.setProjet(projet);
        chargement.setDateChargement(chargementDTO.getDateChargement());

        Chargement savedChargement = chargementRepository.save(chargement);
        return chargementMapper.toDTO(savedChargement);
    }

    @Transactional
    public ChargementDTO updateChargement(Long id, ChargementDTO chargementDTO) {
        Chargement chargement = chargementRepository.findById(id)
                .orElseThrow(() -> new ChargementNotFoundException(id));

        // Valider l'existence des entités liées
        if (chargementDTO.getCamionId() != null) {
            Camion camion = camionRepository.findById(chargementDTO.getCamionId())
                    .orElseThrow(() -> new CamionNotFoundException(chargementDTO.getCamionId()));
            chargement.setCamion(camion);
        }

        if (chargementDTO.getChauffeurId() != null) {
            Chauffeur chauffeur = chauffeurRepository.findById(chargementDTO.getChauffeurId())
                    .orElseThrow(() -> new ChauffeurNotFoundException(chargementDTO.getChauffeurId()));
            chargement.setChauffeur(chauffeur);
        }

        if (chargementDTO.getProjetId() != null) {
            Projet projet = projetRepository.findById(chargementDTO.getProjetId())
                    .orElseThrow(() -> new ProjetNotFoundException(chargementDTO.getProjetId()));
            chargement.setProjet(projet);
        }

        if (chargementDTO.getSociete() != null) {
            chargement.setSociete(chargementDTO.getSociete());
        }

        if (chargementDTO.getSocieteP() != null) {
            chargement.setSocieteP(chargementDTO.getSocieteP());
        }

        if (chargementDTO.getDateChargement() != null) {
            chargement.setDateChargement(chargementDTO.getDateChargement());
        }

        Chargement updatedChargement = chargementRepository.save(chargement);
        return chargementMapper.toDTO(updatedChargement);
    }

    @Transactional
    public void deleteChargement(Long id) {
        if (!chargementRepository.existsById(id)) {
            throw new ChargementNotFoundException(id);
        }
        chargementRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<ChargementDTO> getChargementsByCamion(Long camionId) {
        return chargementRepository.findByCamionId(camionId)
                .stream()
                .map(chargementMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ChargementDTO> getChargementsByChauffeur(Long chauffeurId) {
        return chargementRepository.findByChauffeurId(chauffeurId)
                .stream()
                .map(chargementMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ChargementDTO> getChargementsByProjet(Long projetId) {
        return chargementRepository.findByProjetId(projetId)
                .stream()
                .map(chargementMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ChargementDTO> getChargementsBySociete(String societe) {
        return chargementRepository.findBySociete(societe)
                .stream()
                .map(chargementMapper::toDTO)
                .collect(Collectors.toList());
    }
}
