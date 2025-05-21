package com.ccpc.yeprogress.mapper;

import com.ccpc.yeprogress.dto.CategoryDTO;
import com.ccpc.yeprogress.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDTO toDto(Category category);

    Category toEntity(CategoryDTO categoryDTO);

    void updateEntityFromDto(CategoryDTO categoryDTO, @MappingTarget Category category);
}