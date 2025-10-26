package com.example.navire.mapper;
import com.example.navire.model.Camion;
import com.example.navire.dto.CamionDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CamionMapper {
    CamionDTO toDTO(Camion camion);
    Camion toEntity(CamionDTO dto);
}
