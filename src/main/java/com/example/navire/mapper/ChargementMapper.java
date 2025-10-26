package com.example.navire.mapper;

import com.example.navire.dto.ChargementDTO;
import com.example.navire.model.Chargement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ChargementMapper {

    @Mapping(source = "camion.id", target = "camionId")
    @Mapping(source = "chauffeur.id", target = "chauffeurId")
    @Mapping(source = "projet.id", target = "projetId")
    @Mapping(source = "projet.nomProduit", target = "produit")
    @Mapping(source = "projet.nomNavire", target = "navire")
    @Mapping(source = "projet.port", target = "port")
    ChargementDTO toDTO(Chargement chargement);

    @Mapping(source = "camionId", target = "camion.id")
    @Mapping(source = "chauffeurId", target = "chauffeur.id")
    @Mapping(source = "projetId", target = "projet.id")
    Chargement toEntity(ChargementDTO dto);

    @Mapping(source = "camionId", target = "camion.id")
    @Mapping(source = "chauffeurId", target = "chauffeur.id")
    @Mapping(source = "projetId", target = "projet.id")
    void updateEntityFromDTO(ChargementDTO dto, @MappingTarget Chargement chargement);
}
