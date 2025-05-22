package com.ccpc.yeprogress.mapper;

import com.ccpc.yeprogress.dto.AuthenticationDTO;
import com.ccpc.yeprogress.model.Authentication;
import com.ccpc.yeprogress.model.types.AuthenticationStatusType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface AuthenticationMapper {
    @Mapping(target = "methodName", source = "method.authMethodName")
    @Mapping(target = "statusName", source = "status", qualifiedByName = "enumToDisplayName")
    @Mapping(target = "externalAuthId", source = "externalAuthId")
    @Mapping(target = "verifiedAt", source = "verifiedAt")
    AuthenticationDTO toDto(Authentication authentication);

    @Mapping(target = "method.authMethodName", source = "methodName")
    @Mapping(target = "status", source = "statusName", qualifiedByName = "displayNameToEnum")
    @Mapping(target = "externalAuthId", source = "externalAuthId")
    @Mapping(target = "verifiedAt", source = "verifiedAt")
    Authentication toEntity(AuthenticationDTO authenticationDTO);

    @Mapping(target = "authenticationId", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "method", ignore = true)
    @Mapping(target = "status", ignore = true)
    void updateEntityFromDto(AuthenticationDTO authenticationDTO, @MappingTarget Authentication authentication);

    @Named("enumToDisplayName")
    default String enumToDisplayName(AuthenticationStatusType status) {
        return status != null ? status.getDisplayName() : null;
    }

    @Named("displayNameToEnum")
    default AuthenticationStatusType displayNameToEnum(String displayName) {
        if (displayName == null) {
            return AuthenticationStatusType.getDefault();
        }
        for (AuthenticationStatusType status : AuthenticationStatusType.values()) {
            if (status.getDisplayName().equals(displayName)) {
                return status;
            }
        }
        return AuthenticationStatusType.getDefault();
    }
}