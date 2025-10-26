package com.example.navire.mapper;

import com.example.navire.model.Societe;
import com.example.navire.dto.SocieteDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SocieteMapper {
    SocieteDTO toDTO(Societe societe);

    @Mapping(target = "projets", ignore = true)
    Societe toEntity(SocieteDTO dto);
}
