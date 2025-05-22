package com.ccpc.yeprogress.mapper;

import com.ccpc.yeprogress.dto.AuthenticationMethodDTO;
import com.ccpc.yeprogress.model.AuthenticationMethod;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AuthenticationMethodMapper {
    @Mapping(target = "authMethodName", source = "authMethodName")
    AuthenticationMethodDTO toDto(AuthenticationMethod authenticationMethod);

    @Mapping(target = "authMethodName", source = "authMethodName")
    AuthenticationMethod toEntity(AuthenticationMethodDTO authenticationMethodDTO);

    @Mapping(target = "authMethodId", ignore = true)
    void updateEntityFromDto(AuthenticationMethodDTO authenticationMethodDTO, @MappingTarget AuthenticationMethod authenticationMethod);
}