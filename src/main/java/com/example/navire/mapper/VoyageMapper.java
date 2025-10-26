package com.example.navire.mapper;

import com.example.navire.model.Voyage;
import com.example.navire.dto.VoyageDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface VoyageMapper {
    @Mapping(target = "chauffeurNom", ignore = true)
    @Mapping(target = "camionNom", ignore = true)
    @Mapping(target = "clientNum", ignore = true)
    @Mapping(target = "depotNom", ignore = true)
    @Mapping(source = "autorisationCode", target = "autorisationCode")
    VoyageDTO toDTO(Voyage voyage);

    Voyage toEntity(VoyageDTO dto);

    @AfterMapping
    default void mapCustomFields(Voyage voyage, @MappingTarget VoyageDTO dto) {
        dto.setId(voyage.getId());
        if (voyage.getChauffeur() != null) {
            dto.setChauffeurNom(voyage.getChauffeur().getNom());
            dto.setChauffeurId(voyage.getChauffeur().getId());
        } else {
            dto.setChauffeurId(null);
        }
        if (voyage.getCamion() != null) {
            dto.setCamionNom(voyage.getCamion().getMatricule());
            dto.setCamionId(voyage.getCamion().getId());
        } else {
            dto.setCamionId(null);
        }
        if (voyage.getClient() != null) {
            dto.setClientNum(voyage.getClient().getNumero());
            dto.setClientId(voyage.getClient().getId());
        } else {
            dto.setClientId(null);
        }
        if (voyage.getDepot() != null) {
            dto.setDepotNom(voyage.getDepot().getNom());
            dto.setDepotId(voyage.getDepot().getId());
        } else {
            dto.setDepotId(null);
        }
        if (voyage.getProjet() != null) {
            dto.setProjetId(voyage.getProjet().getId());
        } else {
            dto.setProjetId(null);
        }
        dto.setAutorisationCode(voyage.getAutorisationCode());
        if (voyage.getUser() != null) {
            dto.setUserId(voyage.getUser().getId());
        } else {
            dto.setUserId(null);
        }
    }
}
