package com.ccpc.yeprogress.service;

import com.ccpc.yeprogress.exception.UserNotFoundException;
import com.ccpc.yeprogress.exception.UserValidationException;
import com.ccpc.yeprogress.logger.LoggerService;
import com.ccpc.yeprogress.model.Campaign;
import com.ccpc.yeprogress.repository.CampaignRepository;
import com.ccpc.yeprogress.scraper.MonobankJarScraper;
import com.ccpc.yeprogress.validation.CampaignScrapingValidationService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class CampaignScrapingService {

    private static final Logger logger = LoggerService.getLogger(CampaignScrapingService.class);

    private final CampaignRepository campaignRepository;
    private final CampaignScrapingValidationService scrapingValidationService;

    @Autowired
    public CampaignScrapingService(CampaignRepository campaignRepository,
                                   CampaignScrapingValidationService scrapingValidationService) {
        this.campaignRepository = campaignRepository;
        this.scrapingValidationService = scrapingValidationService;
    }

    @Scheduled(fixedRate = 180000) // 3 minutes in milliseconds
    @Transactional
    public void updateAllCampaignAmounts() {
        LoggerService.logInfo(logger, "Starting update of amounts for all campaigns");

        try {
            List<Campaign> campaigns = campaignRepository.findAll();
            int updatedCount = 0;

            for (Campaign campaign : campaigns) {
                if (campaign.getBankaUrl() != null) {
                    try {
                        scrapingValidationService.validateBankaUrl(campaign.getBankaUrl());
                        boolean updated = updateCampaignAmounts(campaign);
                        if (updated) {
                            updatedCount++;
                        }
                        // Pause between requests to avoid overloading the server
                        Thread.sleep(2000);
                    } catch (UserValidationException e) {
                        LoggerService.logWarn(logger, "Validation failed for campaign ID {}: {}",
                                campaign.getCampaignId(), e.getMessage());
                    } catch (Exception e) {
                        LoggerService.logWarn(logger, "Error updating campaign ID {}: {}",
                                campaign.getCampaignId(), e.getMessage());
                    }
                }
            }

            LoggerService.logInfo(logger, "Updated amounts for {} out of {} campaigns",
                    updatedCount, campaigns.size());

        } catch (Exception e) {
            LoggerService.logUnexpectedError(logger, "batch campaign amount update", e.getMessage(), e);
            throw new RuntimeException("Failed to update campaign amounts due to unexpected error", e);
        }
    }

    @Transactional
    public boolean updateCampaignAmounts(Campaign campaign) {
        LoggerService.logInfo(logger, "Updating amounts for campaign ID: {}", campaign.getCampaignId());

        try {
            if (campaign == null || campaign.getCampaignId() == null) {
                LoggerService.logValidationFailure(logger, "Campaign is required");
                throw new UserValidationException("Campaign is required for amount update");
            }
            scrapingValidationService.validateBankaUrl(campaign.getBankaUrl());

            Map<String, String> scrapedData = MonobankJarScraper.getJarAmounts(campaign.getBankaUrl());

            if (scrapedData != null && !scrapedData.isEmpty()) {
                boolean hasChanges = false;

                if (scrapedData.containsKey("accumulated")) {
                    BigDecimal newCurrentAmount = scrapingValidationService.parseAmountString(scrapedData.get("accumulated"));
                    if (newCurrentAmount != null &&
                            (campaign.getCurrentAmount() == null ||
                                    campaign.getCurrentAmount().compareTo(newCurrentAmount) != 0)) {
                        campaign.setCurrentAmount(newCurrentAmount);
                        hasChanges = true;
                        LoggerService.logInfo(logger, "Updated current amount for campaign ID {}: {}",
                                campaign.getCampaignId(), newCurrentAmount);
                    }
                }

                if (scrapedData.containsKey("goal")) {
                    BigDecimal newGoalAmount = scrapingValidationService.parseAmountString(scrapedData.get("goal"));
                    if (newGoalAmount != null &&
                            (campaign.getGoalAmount() == null ||
                                    campaign.getGoalAmount().compareTo(newGoalAmount) != 0)) {
                        campaign.setGoalAmount(newGoalAmount);
                        hasChanges = true;
                        LoggerService.logInfo(logger, "Updated goal amount for campaign ID {}: {}",
                                campaign.getCampaignId(), newGoalAmount);
                    }
                }

                if (hasChanges) {
                    campaignRepository.save(campaign);
                    LoggerService.logDebug(logger, "Saved updated campaign ID: {}", campaign.getCampaignId());
                    return true;
                }
            }

            LoggerService.logDebug(logger, "No changes detected for campaign ID: {}", campaign.getCampaignId());
            return false;

        } catch (UserValidationException e) {
            LoggerService.logError(logger, "Validation failed for campaign ID {}: {}",
                    campaign.getCampaignId(), e.getMessage());
            throw e;
        } catch (Exception e) {
            LoggerService.logError(logger, "Error scraping data for campaign ID {} with URL {}: {}",
                    campaign.getCampaignId(), campaign.getBankaUrl(), e.getMessage());
            throw new RuntimeException("Failed to update campaign amounts due to unexpected error", e);
        }
    }

    public boolean updateCampaignById(Long campaignId) {
        LoggerService.logInfo(logger, "Attempting to update campaign by ID: {}", campaignId);

        try {
            Campaign campaign = campaignRepository.findById(campaignId)
                    .orElseThrow(() -> {
                        LoggerService.logEntityNotFound(logger, "Campaign", campaignId);
                        return new UserNotFoundException("Campaign with ID " + campaignId + " not found");
                    });

            return updateCampaignAmounts(campaign);

        } catch (UserNotFoundException e) {
            LoggerService.logError(logger, "Error updating campaign by ID {}: {}", campaignId, e.getMessage());
            throw e;
        } catch (Exception e) {
            LoggerService.logUnexpectedError(logger, "campaign update by ID " + campaignId, e.getMessage(), e);
            throw new RuntimeException("Failed to update campaign due to unexpected error", e);
        }
    }
}