package com.ccpc.yeprogress.mapper;

import com.ccpc.yeprogress.dto.CommentsCampaignDTO;
import com.ccpc.yeprogress.model.CommentsCampaign;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CommentsCampaignMapper {
    @Mapping(target = "userId", source = "user.userId")
    @Mapping(target = "campaignId", source = "campaign.campaignId")
    CommentsCampaignDTO toDto(CommentsCampaign commentsCampaign);

    @Mapping(target = "user.userId", source = "userId")
    @Mapping(target = "campaign.campaignId", source = "campaignId")
    CommentsCampaign toEntity(CommentsCampaignDTO commentsCampaignDTO);

    @Mapping(target = "commentId", ignore = true) // Ігноруємо commentId під час оновлення
    void updateEntityFromDto(CommentsCampaignDTO commentsCampaignDTO, @MappingTarget CommentsCampaign commentsCampaign);
}