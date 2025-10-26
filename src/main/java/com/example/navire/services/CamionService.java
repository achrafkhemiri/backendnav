package com.example.navire.services;

import com.example.navire.dto.CamionDTO;
import com.example.navire.exception.CamionNotFoundException;
import com.example.navire.mapper.CamionMapper;
import com.example.navire.model.Camion;
import com.example.navire.repository.CamionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CamionService {
    @Autowired
    private CamionRepository camionRepository;
    @Autowired
    private CamionMapper camionMapper;

    public List<CamionDTO> getAllCamions() {
        return camionRepository.findAll().stream()
                .map(camionMapper::toDTO)
                .collect(Collectors.toList());
    }

    public CamionDTO getCamionById(Long id) {
        Camion camion = camionRepository.findById(id)
                .orElseThrow(() -> new CamionNotFoundException(id));
        return camionMapper.toDTO(camion);
    }

    @Transactional
    public CamionDTO createCamion(CamionDTO dto) {
        if (camionRepository.existsByMatricule(dto.getMatricule())) {
            throw new IllegalArgumentException("Matricule already exists");
        }
        Camion camion = camionMapper.toEntity(dto);
        return camionMapper.toDTO(camionRepository.save(camion));
    }

    @Transactional
    public CamionDTO updateCamion(Long id, CamionDTO dto) {
        Camion camion = camionRepository.findById(id)
                .orElseThrow(() -> new CamionNotFoundException(id));
        camion.setMatricule(dto.getMatricule());
        camion.setSociete(dto.getSociete());
        return camionMapper.toDTO(camionRepository.save(camion));
    }

    @Transactional
    public void deleteCamion(Long id) {
        if (!camionRepository.existsById(id)) {
            throw new CamionNotFoundException(id);
        }
        camionRepository.deleteById(id);
    }
}
