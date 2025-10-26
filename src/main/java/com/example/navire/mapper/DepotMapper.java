package com.example.navire.mapper;

import com.example.navire.model.Depot;
import com.example.navire.dto.DepotDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DepotMapper {
    DepotDTO toDTO(Depot depot);
    Depot toEntity(DepotDTO dto);
    
    /**
     * Convertit un Depot en DepotDTO avec le projetId spécifique
     * Utile pour les relations Many-to-Many
     */
    default DepotDTO toDTOWithProjetId(Depot depot, Long projetId) {
        DepotDTO dto = toDTO(depot);
        dto.setProjetId(projetId);
        return dto;
    }
}
