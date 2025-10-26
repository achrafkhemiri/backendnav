package com.example.navire.mapper;

import com.example.navire.model.Chauffeur;
import com.example.navire.dto.ChauffeurDTO;
import org.mapstruct.*;
import java.util.HashSet;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface ChauffeurMapper {
    @Mapping(target = "numBonLivraisonVoyages", ignore = true)
    ChauffeurDTO toDTO(Chauffeur chauffeur);

    Chauffeur toEntity(ChauffeurDTO dto);

    @AfterMapping
    default void mapNumBonLivraisonVoyages(Chauffeur chauffeur, @MappingTarget ChauffeurDTO dto) {
        Set<String> numBonLivraisonVoyages = new HashSet<>();
        if (chauffeur.getVoyages() != null) {
            chauffeur.getVoyages().forEach(v -> {
                if (v.getNumBonLivraison() != null) {
                    numBonLivraisonVoyages.add(v.getNumBonLivraison());
                }
            });
        }
        dto.setNumBonLivraisonVoyages(numBonLivraisonVoyages);
    }
}
