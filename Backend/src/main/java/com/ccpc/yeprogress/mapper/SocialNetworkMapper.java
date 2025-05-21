package com.ccpc.yeprogress.mapper;

import com.ccpc.yeprogress.dto.SocialNetworkDTO;
import com.ccpc.yeprogress.model.SocialNetwork;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SocialNetworkMapper {
    @Mapping(target = "userId", source = "user.userId")
    SocialNetworkDTO toDto(SocialNetwork socialNetwork);

    SocialNetwork toEntity(SocialNetworkDTO socialNetworkDTO);

    @Mapping(target = "contactId", ignore = true) // Ігноруємо contactId під час оновлення
    void updateEntityFromDto(SocialNetworkDTO socialNetworkDTO, @MappingTarget SocialNetwork socialNetwork);
}