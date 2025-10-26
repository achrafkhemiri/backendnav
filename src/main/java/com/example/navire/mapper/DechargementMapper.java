package com.example.navire.mapper;

import com.example.navire.dto.DechargementDTO;
import com.example.navire.model.Dechargement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DechargementMapper {

    @Mapping(source = "chargement.id", target = "chargementId")
    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "depot.id", target = "depotId")
    @Mapping(source = "chargement.camion.id", target = "camionId")
    @Mapping(source = "chargement.chauffeur.id", target = "chauffeurId")
    @Mapping(source = "chargement.societe", target = "societe")
    @Mapping(source = "chargement.projet.id", target = "projetId")
    @Mapping(source = "chargement.projet.nomProduit", target = "produit")
    @Mapping(source = "chargement.projet.nomNavire", target = "navire")
    @Mapping(source = "chargement.projet.port", target = "port")
    @Mapping(source = "chargement.dateChargement", target = "dateChargement")
    DechargementDTO toDTO(Dechargement dechargement);

    // When mapping DTO->Entity we ignore nested relations (chargement/client/depot)
    // because services set the relation objects (avoid MapStruct creating null nested objects)
    @Mapping(target = "chargement", ignore = true)
    @Mapping(target = "client", ignore = true)
    @Mapping(target = "depot", ignore = true)
    Dechargement toEntity(DechargementDTO dto);

    // For updates, ignore nested relation objects to avoid null-pointer mapping; service will handle them
    @Mapping(target = "chargement", ignore = true)
    @Mapping(target = "client", ignore = true)
    @Mapping(target = "depot", ignore = true)
    void updateEntityFromDTO(DechargementDTO dto, @MappingTarget Dechargement dechargement);
}
