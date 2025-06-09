package com.ccpc.yeprogress.service;

import com.ccpc.yeprogress.dto.CampaignDTO;
import com.ccpc.yeprogress.exception.UserAlreadyExistsException;
import com.ccpc.yeprogress.exception.UserNotFoundException;
import com.ccpc.yeprogress.exception.UserValidationException;
import com.ccpc.yeprogress.logger.LoggerService;
import com.ccpc.yeprogress.mapper.CampaignMapper;
import com.ccpc.yeprogress.model.Campaign;
import com.ccpc.yeprogress.model.User;
import com.ccpc.yeprogress.repository.CampaignRepository;
import com.ccpc.yeprogress.validation.ValidationService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CampaignService {

    private static final Logger logger = LoggerService.getLogger(CampaignService.class);

    private final CampaignRepository campaignRepository;
    private final CampaignMapper campaignMapper;
    private final CampaignScrapingService scrapingService; // Corrected to CampaignScrapingService
    private final ValidationService validationService;

    @Autowired
    public CampaignService(CampaignRepository campaignRepository,
                           CampaignMapper campaignMapper,
                           CampaignScrapingService scrapingService, // Corrected to CampaignScrapingService
                           ValidationService validationService) {
        this.campaignRepository = campaignRepository;
        this.campaignMapper = campaignMapper;
        this.scrapingService = scrapingService;
        this.validationService = validationService;
    }

    @Transactional
    public CampaignDTO createCampaign(User user, CampaignDTO campaignDTO) {
        LoggerService.logCreateAttempt(logger, "Campaign", user != null ? user.getUserId() : "null");

        try {
            if (user == null || user.getUserId() == null) {
                LoggerService.logValidationFailure(logger, "User is required");
                throw new UserValidationException("User is required for campaign creation");
            }
            validationService.validateCampaignDTO(campaignDTO);

            if (StringUtils.hasText(campaignDTO.getBankaUrl())) {
                boolean exists = campaignRepository.existsByBankaUrl(campaignDTO.getBankaUrl());
                if (exists) {
                    LoggerService.logValidationFailure(logger, "Campaign with banka URL {} already exists", campaignDTO.getBankaUrl());
                    throw new UserAlreadyExistsException("Campaign with banka URL " + campaignDTO.getBankaUrl() + " already exists");
                }
            }

            Campaign campaign = campaignMapper.toEntity(campaignDTO);
            campaign.setUser(user);
            Campaign savedCampaign = campaignRepository.save(campaign);
            LoggerService.logCreateSuccess(logger, "Campaign", savedCampaign.getCampaignId());

            if (StringUtils.hasText(savedCampaign.getBankaUrl())) {
                try {
                    scrapingService.updateCampaignAmounts(savedCampaign);
                    LoggerService.logDebug(logger, "Updated amounts for new campaign ID: {}", savedCampaign.getCampaignId());
                } catch (Exception e) {
                    LoggerService.logWarn(logger, "Failed to update amounts for new campaign ID {}: {}", savedCampaign.getCampaignId(), e.getMessage());
                }
            }

            return campaignMapper.toDto(savedCampaign);

        } catch (UserValidationException | UserAlreadyExistsException e) {
            LoggerService.logError(logger, "Error creating campaign: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            LoggerService.logUnexpectedError(logger, "campaign creation", e.getMessage(), e);
            throw new RuntimeException("Failed to create campaign due to unexpected error", e);
        }
    }

    public CampaignDTO getCampaignById(Long id) {
        LoggerService.logRetrieveAttempt(logger, "Campaign", id);

        try {
            Campaign campaign = campaignRepository.findById(id)
                    .orElseThrow(() -> {
                        LoggerService.logEntityNotFound(logger, "Campaign", id);
                        return new UserNotFoundException("Campaign with ID " + id + " not found");
                    });

            LoggerService.logRetrieveSuccess(logger, "Campaign", id);
            return campaignMapper.toDto(campaign);

        } catch (UserNotFoundException e) {
            LoggerService.logError(logger, "Error retrieving campaign: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            LoggerService.logUnexpectedError(logger, "campaign retrieval", e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve campaign due to unexpected error", e);
        }
    }

    public List<CampaignDTO> getCampaignsByUserId(Long userId) {
        LoggerService.logRetrieveAttempt(logger, "Campaigns for user", userId);

        try {
            List<Campaign> campaigns = campaignRepository.findByUser_UserId(userId);
            LoggerService.logRetrieveSuccess(logger, "Campaigns for user", campaigns.size());

            return campaigns.stream()
                    .map(campaignMapper::toDto)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            LoggerService.logUnexpectedError(logger, "retrieval of campaigns for user ID " + userId, e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve campaigns due to unexpected error", e);
        }
    }

    public List<CampaignDTO> getAllCampaigns() {
        LoggerService.logRetrieveAttempt(logger, "All Campaigns", "all");

        try {
            List<Campaign> campaigns = campaignRepository.findAll();
            LoggerService.logRetrieveSuccess(logger, "All Campaigns", campaigns.size());

            return campaigns.stream()
                    .map(campaignMapper::toDto)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            LoggerService.logUnexpectedError(logger, "retrieval of all campaigns", e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve campaigns due to unexpected error", e);
        }
    }

    @Transactional
    public CampaignDTO updateCampaign(Long id, CampaignDTO campaignDTO) {
        LoggerService.logUpdateAttempt(logger, "Campaign", id);

        try {
            validationService.validateCampaignDTO(campaignDTO);

            Campaign campaign = campaignRepository.findById(id)
                    .orElseThrow(() -> {
                        LoggerService.logEntityNotFound(logger, "Campaign", id);
                        return new UserNotFoundException("Campaign with ID " + id + " not found");
                    });

            String oldBankaUrl = campaign.getBankaUrl();
            if (StringUtils.hasText(campaignDTO.getBankaUrl()) &&
                    !campaignDTO.getBankaUrl().equals(oldBankaUrl)) {
                boolean exists = campaignRepository.existsByBankaUrl(campaignDTO.getBankaUrl());
                if (exists) {
                    LoggerService.logValidationFailure(logger, "Campaign with banka URL {} already exists", campaignDTO.getBankaUrl());
                    throw new UserAlreadyExistsException("Campaign with banka URL " + campaignDTO.getBankaUrl() + " already exists");
                }
            }

            campaignMapper.updateEntityFromDto(campaignDTO, campaign);
            Campaign savedCampaign = campaignRepository.save(campaign);
            LoggerService.logUpdateSuccess(logger, "Campaign", id);

            if (StringUtils.hasText(savedCampaign.getBankaUrl()) &&
                    !savedCampaign.getBankaUrl().equals(oldBankaUrl)) {
                try {
                    scrapingService.updateCampaignAmounts(savedCampaign);
                    LoggerService.logDebug(logger, "Updated amounts for campaign ID: {}", savedCampaign.getCampaignId());
                } catch (Exception e) {
                    LoggerService.logWarn(logger, "Failed to update amounts for campaign ID {}: {}", savedCampaign.getCampaignId(), e.getMessage());
                }
            }

            return campaignMapper.toDto(savedCampaign);

        } catch (UserValidationException | UserNotFoundException | UserAlreadyExistsException e) {
            LoggerService.logError(logger, "Error updating campaign: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            LoggerService.logUnexpectedError(logger, "campaign update", e.getMessage(), e);
            throw new RuntimeException("Failed to update campaign due to unexpected error", e);
        }
    }

    @Transactional
    public void deleteCampaign(Long id) {
        LoggerService.logDeleteAttempt(logger, "Campaign", id);

        try {
            if (!campaignRepository.existsById(id)) {
                LoggerService.logEntityNotFound(logger, "Campaign", id);
                throw new UserNotFoundException("Campaign with ID " + id + " not found");
            }

            campaignRepository.deleteById(id);
            LoggerService.logDeleteSuccess(logger, "Campaign", id);

        } catch (UserNotFoundException e) {
            LoggerService.logError(logger, "Error deleting campaign: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            LoggerService.logUnexpectedError(logger, "campaign deletion", e.getMessage(), e);
            throw new RuntimeException("Failed to delete campaign due to unexpected error", e);
        }
    }

    @Transactional
    public CampaignDTO refreshCampaignData(Long id) {
        LoggerService.logRetrieveAttempt(logger, "Campaign data refresh", id);

        try {
            Campaign campaign = campaignRepository.findById(id)
                    .orElseThrow(() -> {
                        LoggerService.logEntityNotFound(logger, "Campaign", id);
                        return new UserNotFoundException("Campaign with ID " + id + " not found");
                    });

            if (!StringUtils.hasText(campaign.getBankaUrl())) {
                LoggerService.logValidationFailure(logger, "Campaign ID {} has no banka URL", id);
                throw new UserValidationException("Campaign has no banka URL to refresh from");
            }

            boolean updated = scrapingService.updateCampaignAmounts(campaign);
            if (!updated) {
                LoggerService.logWarn(logger, "Failed to refresh campaign data for ID: {}", id);
                throw new RuntimeException("Failed to refresh campaign data from banka URL");
            }

            LoggerService.logRetrieveSuccess(logger, "Campaign data refresh", id);
            return campaignMapper.toDto(campaign);

        } catch (UserNotFoundException | UserValidationException e) {
            LoggerService.logError(logger, "Error refreshing campaign data: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            LoggerService.logUnexpectedError(logger, "campaign data refresh", e.getMessage(), e);
            throw new RuntimeException("Failed to refresh campaign data due to unexpected error", e);
        }
    }
}