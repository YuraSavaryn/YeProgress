package com.ccpc.yeprogress.mapper;

import com.ccpc.yeprogress.dto.CommentsUserDTO;
import com.ccpc.yeprogress.model.CommentsUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CommentsUserMapper {
    @Mapping(target = "userId", source = "user.userId")
    @Mapping(target = "userAuthorId", source = "userAuthor.userId")
    CommentsUserDTO toDto(CommentsUser commentsUser);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "userAuthor", ignore = true)
    CommentsUser toEntity(CommentsUserDTO commentsUserDTO);

    @Mapping(target = "commentId", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "userAuthor", ignore = true)
    void updateEntityFromDto(CommentsUserDTO commentsUserDTO, @MappingTarget CommentsUser commentsUser);
}