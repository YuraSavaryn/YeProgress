package com.ccpc.yeprogress.mapper;

import com.ccpc.yeprogress.dto.CampaignStatusDTO;
import com.ccpc.yeprogress.model.CampaignStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CampaignStatusMapper {
    // When mapping from CampaignStatus to CampaignStatusDTO, only map 'status' as CampaignStatusDTO does not have 'campaignStatusId'.
    // No explicit @Mapping is needed for 'status' if names match.
    CampaignStatusDTO toDto(CampaignStatus campaignStatus);

    // When mapping from CampaignStatusDTO to CampaignStatus, ignore 'campaignStatusId' as it's typically auto-generated.
    @Mapping(target = "campaignStatusId", ignore = true)
    CampaignStatus toEntity(CampaignStatusDTO campaignStatusDTO);

    // When updating an existing CampaignStatus from CampaignStatusDTO, ignore 'campaignStatusId'.
    @Mapping(target = "campaignStatusId", ignore = true)
    void updateEntityFromDto(CampaignStatusDTO campaignStatusDTO, @MappingTarget CampaignStatus campaignStatus);
}