package com.ccpc.yeprogress.mapper;

import com.ccpc.yeprogress.dto.UserDTO;
import com.ccpc.yeprogress.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "userId", source = "userId")
    UserDTO toDto(User user);

    @Mapping(target = "userId", source = "userId")
    User toEntity(UserDTO UserDTO);

    @Mapping(target = "userId", ignore = true) // Ігноруємо userId під час оновлення
    void updateEntityFromDto(UserDTO UserDTO, @MappingTarget User user);
}
