package com.ccpc.yeprogress.mapper;

import com.ccpc.yeprogress.dto.CampaignImagesDTO;
import com.ccpc.yeprogress.model.CampaignImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CampaignImageMapper {
    @Mapping(target = "imgUrl", source = "imgUrl")
    CampaignImagesDTO toDto(CampaignImage campaignImage);

    @Mapping(target = "imgUrl", source = "imgUrl")
    CampaignImage toEntity(CampaignImagesDTO campaignImagesDTO);

    @Mapping(target = "campaignImageId", ignore = true)
    @Mapping(target = "campaign", ignore = true)
    void updateEntityFromDto(CampaignImagesDTO campaignImagesDTO, @MappingTarget CampaignImage campaignImage);
}