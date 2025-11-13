package com.example.navire.services;

import com.example.navire.dto.SocieteDTO;
import com.example.navire.mapper.SocieteMapper;
import com.example.navire.model.Societe;
import com.example.navire.repository.SocieteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SocieteService {
    
    @Autowired
    private SocieteRepository societeRepository;
    
    @Autowired
    private SocieteMapper societeMapper;

    public List<SocieteDTO> getAllSocietes() {
        return societeRepository.findAll().stream()
                .map(societeMapper::toDTO)
                .collect(Collectors.toList());
    }

    public SocieteDTO getSocieteById(Long id) {
        Societe societe = societeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Société non trouvée avec l'id: " + id));
        return societeMapper.toDTO(societe);
    }

    @Transactional
    public SocieteDTO createSociete(SocieteDTO dto) {
        // Vérifier si une société avec ce nom existe déjà
        if (societeRepository.findByNom(dto.getNom()).isPresent()) {
            throw new RuntimeException("Une société avec ce nom existe déjà");
        }
        Societe societe = societeMapper.toEntity(dto);
        return societeMapper.toDTO(societeRepository.save(societe));
    }

    @Transactional
    public SocieteDTO updateSociete(Long id, SocieteDTO dto) {
        Societe societe = societeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Société non trouvée avec l'id: " + id));
        
        // Vérifier si le nouveau nom n'est pas déjà pris par une autre société
        societeRepository.findByNom(dto.getNom()).ifPresent(s -> {
            if (!s.getId().equals(id)) {
                throw new RuntimeException("Une autre société avec ce nom existe déjà");
            }
        });
        
        societe.setNom(dto.getNom());
        societe.setAdresse(dto.getAdresse());
        societe.setRcs(dto.getRcs());
        societe.setContact(dto.getContact());
        societe.setTva(dto.getTva());
        societe.setLogo(dto.getLogo());
        societe.setDescription(dto.getDescription());
        
        System.out.println("💾 Sauvegarde société - Logo présent: " + (societe.getLogo() != null && !societe.getLogo().isEmpty()) + 
                          ", Description: " + (societe.getDescription() != null ? societe.getDescription().substring(0, Math.min(30, societe.getDescription().length())) + "..." : "null"));
        
        return societeMapper.toDTO(societeRepository.save(societe));
    }

    @Transactional
    public void deleteSociete(Long id) {
        if (!societeRepository.existsById(id)) {
            throw new RuntimeException("Société non trouvée avec l'id: " + id);
        }
        societeRepository.deleteById(id);
    }
}
