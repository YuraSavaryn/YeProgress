package com.ccpc.yeprogress.mapper;

import com.ccpc.yeprogress.dto.CampaignDTO;
import com.ccpc.yeprogress.model.Campaign;
import com.ccpc.yeprogress.model.types.CampaignStatusType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface CampaignMapper {
    @Mapping(target = "campaignId", source = "campaignId")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "bankaUrl", source = "bankaUrl")
    @Mapping(target = "currentAmount", source = "currentAmount")
    @Mapping(target = "goalAmount", source = "goalAmount")
    @Mapping(target = "createdDate", source = "createdDate")
    @Mapping(target = "approxDeadline", source = "approxDeadline")
    @Mapping(target = "status", source = "status", qualifiedByName = "enumToDisplayName")
    @Mapping(target = "mainImgUrl", source = "mainImgUrl")
    CampaignDTO toDto(Campaign campaign);

    @Mapping(target = "campaignId", source = "campaignId")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "bankaUrl", source = "bankaUrl")
    @Mapping(target = "currentAmount", source = "currentAmount")
    @Mapping(target = "goalAmount", source = "goalAmount")
    @Mapping(target = "createdDate", source = "createdDate")
    @Mapping(target = "approxDeadline", source = "approxDeadline")
    @Mapping(target = "status", source = "status", qualifiedByName = "displayNameToEnum")
    @Mapping(target = "mainImgUrl", source = "mainImgUrl")
    Campaign toEntity(CampaignDTO campaignDTO);

    @Mapping(target = "campaignId", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "categoryId", ignore = true)
    @Mapping(target = "status", ignore = true)
    void updateEntityFromDto(CampaignDTO campaignDTO, @MappingTarget Campaign campaign);

    @Named("enumToDisplayName")
    default String enumToDisplayName(CampaignStatusType status) {
        return status != null ? status.getDisplayName() : null;
    }

    @Named("displayNameToEnum")
    default CampaignStatusType displayNameToEnum(String displayName) {
        if (displayName == null) {
            return CampaignStatusType.getDefault();
        }
        for (CampaignStatusType status : CampaignStatusType.values()) {
            if (status.getDisplayName().equals(displayName)) {
                return status;
            }
        }
        return CampaignStatusType.getDefault();
    }
}