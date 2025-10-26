package com.example.navire.services;

import com.example.navire.dto.ChauffeurDTO;
import com.example.navire.exception.ChauffeurNotFoundException;
import com.example.navire.mapper.ChauffeurMapper;
import com.example.navire.model.Chauffeur;
import com.example.navire.repository.ChauffeurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChauffeurService {
    @Autowired
    private ChauffeurRepository chauffeurRepository;
    @Autowired
    private ChauffeurMapper chauffeurMapper;

    public List<ChauffeurDTO> getAllChauffeurs() {
        return chauffeurRepository.findAll().stream()
                .map(chauffeurMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ChauffeurDTO getChauffeurById(Long id) {
        Chauffeur chauffeur = chauffeurRepository.findById(id)
                .orElseThrow(() -> new ChauffeurNotFoundException(id));
        return chauffeurMapper.toDTO(chauffeur);
    }

    @Transactional
    public ChauffeurDTO createChauffeur(ChauffeurDTO dto) {
        Chauffeur chauffeur = chauffeurMapper.toEntity(dto);
        return chauffeurMapper.toDTO(chauffeurRepository.save(chauffeur));
    }

    @Transactional
    public ChauffeurDTO updateChauffeur(Long id, ChauffeurDTO dto) {
        Chauffeur chauffeur = chauffeurRepository.findById(id)
                .orElseThrow(() -> new ChauffeurNotFoundException(id));
        chauffeur.setNom(dto.getNom());
        return chauffeurMapper.toDTO(chauffeurRepository.save(chauffeur));
    }

    @Transactional
    public void deleteChauffeur(Long id) {
        if (!chauffeurRepository.existsById(id)) {
            throw new ChauffeurNotFoundException(id);
        }
        chauffeurRepository.deleteById(id);
    }
}
