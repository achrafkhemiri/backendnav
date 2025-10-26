package com.example.navire.mapper;

import com.example.navire.model.User;
import com.example.navire.dto.LoginDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    LoginDTO toDTO(User user);
    User toEntity(LoginDTO dto);
}
