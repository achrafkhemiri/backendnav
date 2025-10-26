package com.example.navire.mapper;

import com.example.navire.model.ProjetClient;
import com.example.navire.dto.ProjetClientDTO;
import com.example.navire.dto.AutorisationDTO;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ProjetClientMapper {
    @Mapping(source = "projet.id", target = "projetId")
    @Mapping(source = "client.id", target = "clientId")
    ProjetClientDTO toDTO(ProjetClient projetClient);
    
    ProjetClient toEntity(ProjetClientDTO dto);

    @AfterMapping
    default void mapAutorisation(ProjetClient pc, @MappingTarget ProjetClientDTO dto) {
        if (pc == null) return;
        if (pc.getAutorisation() != null) {
            Set<AutorisationDTO> set = pc.getAutorisation().stream()
                    .map(a -> new AutorisationDTO(a.getCode(), a.getQuantite()))
                    .collect(Collectors.toSet());
            dto.setAutorisation(set);
        }
        // Keep legacy quantiteAutorisee populated for backward compatibility
        dto.setQuantiteAutorisee(pc.getQuantiteAutorisee());
    }
}
