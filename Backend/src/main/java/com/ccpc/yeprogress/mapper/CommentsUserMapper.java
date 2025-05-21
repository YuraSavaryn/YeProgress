package com.ccpc.yeprogress.mapper;

import com.ccpc.yeprogress.dto.CommentsUserDTO;
import com.ccpc.yeprogress.model.CommentsUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CommentsUserMapper {
    @Mapping(target = "userId", source = "user.userId")
    CommentsUserDTO toDto(CommentsUser commentsUser);

    CommentsUser toEntity(CommentsUserDTO commentsUserDTO);

    @Mapping(target = "commentId", ignore = true) // Ігноруємо commentId під час оновлення
    void updateEntityFromDto(CommentsUserDTO commentsUserDTO, @MappingTarget CommentsUser commentsUser);
}