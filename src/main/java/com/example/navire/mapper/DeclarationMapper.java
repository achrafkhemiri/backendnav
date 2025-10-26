package com.example.navire.mapper;

import com.example.navire.model.Declaration;
import com.example.navire.dto.DeclarationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DeclarationMapper {
    
    @Mapping(source = "projet.id", target = "projetId")
    DeclarationDTO toDTO(Declaration declaration);
    
    @Mapping(source = "projetId", target = "projet.id")
    Declaration toEntity(DeclarationDTO dto);
}
