package com.example.navire.mapper;

import com.example.navire.model.ProjetDepot;
import com.example.navire.dto.ProjetDepotDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ProjetDepotMapper {
    @Mapping(source = "projet.id", target = "projetId")
    @Mapping(source = "depot.id", target = "depotId")
    @Mapping(source = "depot.nom", target = "depotNom")
    @Mapping(source = "projet.nomProduit", target = "projetNom")
    ProjetDepotDTO toDTO(ProjetDepot projetDepot);
    
    @Mapping(target = "projet", ignore = true)
    @Mapping(target = "depot", ignore = true)
    ProjetDepot toEntity(ProjetDepotDTO dto);
}
