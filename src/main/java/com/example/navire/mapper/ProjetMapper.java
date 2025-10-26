package com.example.navire.mapper;

import com.example.navire.model.Projet;
import com.example.navire.dto.ProjetDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.example.navire.model.Societe;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {SocieteMapper.class})
public interface ProjetMapper {

    @Mapping(source = "societes", target = "societes")
    @Mapping(source = "societes", target = "societeNoms", qualifiedByName = "societesToNoms")
    ProjetDTO toDTO(Projet projet);

    @Mapping(target = "societes", ignore = true)
    @Mapping(target = "voyages", ignore = true)
    @Mapping(target = "projetClients", ignore = true)
    @Mapping(target = "projetDepots", ignore = true)
    @Mapping(target = "declarations", ignore = true)
    Projet toEntity(ProjetDTO dto);

    @Named("societesToNoms")
    default Set<String> societesToNoms(Set<Societe> societes) {
        if (societes == null) return null;
        return societes.stream().map(Societe::getNom).collect(Collectors.toSet());
    }
}
