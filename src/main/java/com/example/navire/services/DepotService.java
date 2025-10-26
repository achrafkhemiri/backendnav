package com.example.navire.services;

import com.example.navire.dto.DepotDTO;
import com.example.navire.exception.DepotNotFoundException;
import com.example.navire.mapper.DepotMapper;
import com.example.navire.model.Depot;
import com.example.navire.repository.DepotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepotService {
    @Autowired
    private DepotRepository depotRepository;
    @Autowired
    private DepotMapper depotMapper;

    public List<DepotDTO> getAllDepots() {
        return depotRepository.findAll().stream()
                .map(depotMapper::toDTO)
                .collect(Collectors.toList());
    }

    public DepotDTO getDepotById(Long id) {
        Depot depot = depotRepository.findById(id)
                .orElseThrow(() -> new DepotNotFoundException(id));
        return depotMapper.toDTO(depot);
    }

    @Transactional
    public DepotDTO createDepot(DepotDTO dto) {
        System.out.println("=== CREATE DEPOT ===");
        System.out.println("DTO reçu - nom: " + dto.getNom());
        System.out.println("DTO reçu - adresse: " + dto.getAdresse());
        System.out.println("DTO reçu - mf: " + dto.getMf());
        
        if (depotRepository.existsByNom(dto.getNom())) {
            throw new IllegalArgumentException("Nom already exists");
        }
        Depot depot = depotMapper.toEntity(dto);
        
        System.out.println("Entity mappé - nom: " + depot.getNom());
        System.out.println("Entity mappé - adresse: " + depot.getAdresse());
        System.out.println("Entity mappé - mf: " + depot.getMf());
        
        Depot saved = depotRepository.save(depot);
        
        System.out.println("Entity sauvegardé - id: " + saved.getId());
        System.out.println("Entity sauvegardé - adresse: " + saved.getAdresse());
        System.out.println("Entity sauvegardé - mf: " + saved.getMf());
        
        return depotMapper.toDTO(saved);
    }

    @Transactional
    public DepotDTO updateDepot(Long id, DepotDTO dto) {
        Depot depot = depotRepository.findById(id)
                .orElseThrow(() -> new DepotNotFoundException(id));
        depot.setNom(dto.getNom());
        depot.setAdresse(dto.getAdresse());
        depot.setMf(dto.getMf());
        return depotMapper.toDTO(depotRepository.save(depot));
    }

    @Transactional
    public void deleteDepot(Long id) {
        Depot depot = depotRepository.findById(id)
                .orElseThrow(() -> new DepotNotFoundException(id));
        
        // Les ProjetDepot seront supprimés automatiquement grâce à cascade = CascadeType.ALL, orphanRemoval = true
        depotRepository.deleteById(id);
    }
}
