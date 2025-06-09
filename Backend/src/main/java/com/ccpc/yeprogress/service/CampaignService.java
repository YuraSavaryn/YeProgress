package com.ccpc.yeprogress.service;

import com.ccpc.yeprogress.dto.CampaignDTO;
import com.ccpc.yeprogress.exception.UserAlreadyExistsException;
import com.ccpc.yeprogress.exception.UserNotFoundException;
import com.ccpc.yeprogress.exception.UserValidationException;
import com.ccpc.yeprogress.mapper.CampaignMapper;
import com.ccpc.yeprogress.model.Campaign;
import com.ccpc.yeprogress.model.User;
import com.ccpc.yeprogress.model.types.CampaignStatusType;
import com.ccpc.yeprogress.repository.CampaignRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CampaignService {

    private static final Logger logger = LoggerFactory.getLogger(CampaignService.class);

    private final CampaignRepository campaignRepository;
    private final CampaignMapper campaignMapper;
    private final CampaignScrapingService scrapingService;

    private static final int MAX_TITLE_LENGTH = 100;
    private static final int MAX_DESCRIPTION_LENGTH = 500;

    @Autowired
    public CampaignService(CampaignRepository campaignRepository,
                           CampaignMapper campaignMapper,
                           CampaignScrapingService scrapingService) {
        this.campaignRepository = campaignRepository;
        this.campaignMapper = campaignMapper;
        this.scrapingService = scrapingService;
    }

    public CampaignDTO createCampaign(User user, CampaignDTO campaignDTO) {
        logger.info("Attempting to create campaign for user ID: {}",
                user != null ? user.getUserId() : "null");

        try {
            if (user == null || user.getUserId() == null) {
                logger.warn("Validation failed: User is required");
                throw new UserValidationException("User is required for campaign creation");
            }
            validateCampaignDTO(campaignDTO);

            if (StringUtils.hasText(campaignDTO.getBankaUrl())) {
                boolean exists = campaignRepository.existsByBankaUrl(campaignDTO.getBankaUrl());
                if (exists) {
                    logger.warn("Campaign creation failed: Banka URL {} already exists",
                            campaignDTO.getBankaUrl());
                    throw new UserAlreadyExistsException(
                            "Campaign with banka URL " + campaignDTO.getBankaUrl() + " already exists");
                }
            }

            Campaign campaign = campaignMapper.toEntity(campaignDTO);
            campaign.setUser(user);
            Campaign savedCampaign = campaignRepository.save(campaign);
            logger.info("Successfully created campaign with ID: {} for user ID: {}",
                    savedCampaign.getCampaignId(), user.getUserId());

            if (StringUtils.hasText(savedCampaign.getBankaUrl())) {
                try {
                    scrapingService.updateCampaignAmounts(savedCampaign);
                    logger.debug("Updated amounts for new campaign ID: {}",
                            savedCampaign.getCampaignId());
                } catch (Exception e) {
                    logger.warn("Failed to update amounts for new campaign ID {}: {}",
                            savedCampaign.getCampaignId(), e.getMessage());
                }
            }

            return campaignMapper.toDto(savedCampaign);

        } catch (UserValidationException | UserAlreadyExistsException e) {
            logger.error("Error creating campaign: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error during campaign creation: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create campaign due to unexpected error", e);
        }
    }

    public CampaignDTO getCampaignById(Long id) {
        logger.info("Retrieving campaign with ID: {}", id);

        try {
            Campaign campaign = campaignRepository.findById(id)
                    .orElseThrow(() -> {
                        logger.warn("Campaign with ID {} not found", id);
                        return new UserNotFoundException("Campaign with ID " + id + " not found");
                    });

            logger.debug("Successfully retrieved campaign with ID: {}", id);
            return campaignMapper.toDto(campaign);

        } catch (UserNotFoundException e) {
            logger.error("Error retrieving campaign: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error retrieving campaign: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve campaign due to unexpected error", e);
        }
    }

    public List<CampaignDTO> getCampaignsByUserId(Long userId) {
        logger.info("Retrieving campaigns for user ID: {}", userId);

        try {
            List<Campaign> campaigns = campaignRepository.findByUser_UserId(userId);
            logger.debug("Retrieved {} campaigns for user ID: {}", campaigns.size(), userId);

            return campaigns.stream()
                    .map(campaignMapper::toDto)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            logger.error("Unexpected error retrieving campaigns for user ID {}: {}",
                    userId, e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve campaigns due to unexpected error", e);
        }
    }

    public List<CampaignDTO> getAllCampaigns() {
        logger.info("Retrieving all campaigns");

        try {
            List<Campaign> campaigns = campaignRepository.findAll();
            logger.debug("Retrieved {} campaigns", campaigns.size());

            return campaigns.stream()
                    .map(campaignMapper::toDto)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            logger.error("Unexpected error retrieving all campaigns: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve campaigns due to unexpected error", e);
        }
    }

    public CampaignDTO updateCampaign(Long id, CampaignDTO campaignDTO) {
        logger.info("Attempting to update campaign with ID: {}", id);

        try {
            validateCampaignDTO(campaignDTO);

            Campaign campaign = campaignRepository.findById(id)
                    .orElseThrow(() -> {
                        logger.warn("Campaign with ID {} not found", id);
                        return new UserNotFoundException("Campaign with ID " + id + " not found");
                    });

            String oldBankaUrl = campaign.getBankaUrl();
            if (StringUtils.hasText(campaignDTO.getBankaUrl()) &&
                    !campaignDTO.getBankaUrl().equals(oldBankaUrl)) {
                boolean exists = campaignRepository.existsByBankaUrl(campaignDTO.getBankaUrl());
                if (exists) {
                    logger.warn("Campaign update failed: Banka URL {} already exists",
                            campaignDTO.getBankaUrl());
                    throw new UserAlreadyExistsException(
                            "Campaign with banka URL " + campaignDTO.getBankaUrl() + " already exists");
                }
            }

            campaignMapper.updateEntityFromDto(campaignDTO, campaign);
            Campaign savedCampaign = campaignRepository.save(campaign);
            logger.info("Successfully updated campaign with ID: {}", id);

            if (StringUtils.hasText(savedCampaign.getBankaUrl()) &&
                    !savedCampaign.getBankaUrl().equals(oldBankaUrl)) {
                try {
                    scrapingService.updateCampaignAmounts(savedCampaign);
                    logger.debug("Updated amounts for campaign ID: {}", savedCampaign.getCampaignId());
                } catch (Exception e) {
                    logger.warn("Failed to update amounts for campaign ID {}: {}",
                            savedCampaign.getCampaignId(), e.getMessage());
                }
            }

            return campaignMapper.toDto(savedCampaign);

        } catch (UserValidationException | UserNotFoundException | UserAlreadyExistsException e) {
            logger.error("Error updating campaign: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error during campaign update: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update campaign due to unexpected error", e);
        }
    }

    public void deleteCampaign(Long id) {
        logger.info("Attempting to delete campaign with ID: {}", id);

        try {
            if (!campaignRepository.existsById(id)) {
                logger.warn("Campaign deletion failed: ID {} not found", id);
                throw new UserNotFoundException("Campaign with ID " + id + " not found");
            }

            campaignRepository.deleteById(id);
            logger.info("Successfully deleted campaign with ID: {}", id);

        } catch (UserNotFoundException e) {
            logger.error("Error deleting campaign: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error during campaign deletion: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to delete campaign due to unexpected error", e);
        }
    }

    public CampaignDTO refreshCampaignData(Long id) {
        logger.info("Attempting to refresh campaign data for ID: {}", id);

        try {
            Campaign campaign = campaignRepository.findById(id)
                    .orElseThrow(() -> {
                        logger.warn("Campaign with ID {} not found", id);
                        return new UserNotFoundException("Campaign with ID " + id + " not found");
                    });

            if (!StringUtils.hasText(campaign.getBankaUrl())) {
                logger.warn("Refresh failed: Campaign ID {} has no banka URL", id);
                throw new UserValidationException("Campaign has no banka URL to refresh from");
            }

            boolean updated = scrapingService.updateCampaignAmounts(campaign);
            if (!updated) {
                logger.warn("Failed to refresh campaign data for ID: {}", id);
                throw new RuntimeException("Failed to refresh campaign data from banka URL");
            }

            logger.info("Successfully refreshed campaign data for ID: {}", id);
            return campaignMapper.toDto(campaign);

        } catch (UserNotFoundException | UserValidationException e) {
            logger.error("Error refreshing campaign data: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error refreshing campaign data: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to refresh campaign data due to unexpected error", e);
        }
    }

    private void validateCampaignDTO(CampaignDTO campaignDTO) {
        logger.debug("Validating campaign DTO");

        if (!StringUtils.hasText(campaignDTO.getTitle())) {
            logger.warn("Validation failed: Campaign title is required");
            throw new UserValidationException("Campaign title is required");
        }

        if (campaignDTO.getTitle().length() > MAX_TITLE_LENGTH) {
            logger.warn("Validation failed: Campaign title exceeds {} characters", MAX_TITLE_LENGTH);
            throw new UserValidationException(
                    "Campaign title must not exceed " + MAX_TITLE_LENGTH + " characters");
        }

        if (StringUtils.hasText(campaignDTO.getDescription()) &&
                campaignDTO.getDescription().length() > MAX_DESCRIPTION_LENGTH) {
            logger.warn("Validation failed: Campaign description exceeds {} characters",
                    MAX_DESCRIPTION_LENGTH);
            throw new UserValidationException(
                    "Campaign description must not exceed " + MAX_DESCRIPTION_LENGTH + " characters");
        }

        if (StringUtils.hasText(campaignDTO.getBankaUrl()) &&
                !isValidUrl(campaignDTO.getBankaUrl())) {
            logger.warn("Validation failed: Invalid banka URL format: {}",
                    campaignDTO.getBankaUrl());
            throw new UserValidationException("Invalid banka URL format");
        }

        if (StringUtils.hasText(campaignDTO.getMainImgUrl()) &&
                !isValidUrl(campaignDTO.getMainImgUrl())) {
            logger.warn("Validation failed: Invalid main image URL format: {}",
                    campaignDTO.getMainImgUrl());
            throw new UserValidationException("Invalid main image URL format");
        }

        if (StringUtils.hasText(campaignDTO.getStatus())) {
            try {
                CampaignStatusType.valueOf(campaignDTO.getStatus());
            } catch (IllegalArgumentException e) {
                logger.warn("Validation failed: Invalid campaign status: {}",
                        campaignDTO.getStatus());
                throw new UserValidationException("Invalid campaign status: " +
                        campaignDTO.getStatus());
            }
        }
    }

    private boolean isValidUrl(String url) {
        try {
            new URL(url);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}