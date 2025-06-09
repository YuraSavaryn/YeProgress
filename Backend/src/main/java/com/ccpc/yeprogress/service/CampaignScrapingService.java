package com.ccpc.yeprogress.service;

import com.ccpc.yeprogress.exception.UserNotFoundException;
import com.ccpc.yeprogress.exception.UserValidationException;
import com.ccpc.yeprogress.model.Campaign;
import com.ccpc.yeprogress.repository.CampaignRepository;
import com.ccpc.yeprogress.scraper.MonobankJarScraper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CampaignScrapingService {

    private static final Logger logger = LoggerFactory.getLogger(CampaignScrapingService.class);

    private final CampaignRepository campaignRepository;

    @Autowired
    public CampaignScrapingService(CampaignRepository campaignRepository) {
        this.campaignRepository = campaignRepository;
    }

    @Scheduled(fixedRate = 180000) // 3 minutes in milliseconds
    @Transactional
    public void updateAllCampaignAmounts() {
        logger.info("Starting update of amounts for all campaigns");

        try {
            List<Campaign> campaigns = campaignRepository.findAll();
            int updatedCount = 0;

            for (Campaign campaign : campaigns) {
                if (StringUtils.hasText(campaign.getBankaUrl())) {
                    try {
                        validateBankaUrl(campaign.getBankaUrl());
                        boolean updated = updateCampaignAmounts(campaign);
                        if (updated) {
                            updatedCount++;
                        }
                        // Pause between requests to avoid overloading the server
                        Thread.sleep(2000);
                    } catch (UserValidationException e) {
                        logger.warn("Validation failed for campaign ID {}: {}",
                                campaign.getCampaignId(), e.getMessage());
                    } catch (Exception e) {
                        logger.warn("Error updating campaign ID {}: {}",
                                campaign.getCampaignId(), e.getMessage());
                    }
                }
            }

            logger.info("Updated amounts for {} out of {} campaigns", updatedCount, campaigns.size());

        } catch (Exception e) {
            logger.error("Unexpected error during batch campaign amount update: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update campaign amounts due to unexpected error", e);
        }
    }

    @Transactional
    public boolean updateCampaignAmounts(Campaign campaign) {
        logger.info("Updating amounts for campaign ID: {}", campaign.getCampaignId());

        try {
            if (campaign == null || campaign.getCampaignId() == null) {
                logger.warn("Invalid campaign provided for update");
                throw new UserValidationException("Campaign is required for amount update");
            }
            validateBankaUrl(campaign.getBankaUrl());

            Map<String, String> scrapedData = MonobankJarScraper.getJarAmounts(campaign.getBankaUrl());

            if (scrapedData != null && !scrapedData.isEmpty()) {
                boolean hasChanges = false;

                if (scrapedData.containsKey("accumulated")) {
                    BigDecimal newCurrentAmount = parseAmountString(scrapedData.get("accumulated"));
                    if (newCurrentAmount != null &&
                            (campaign.getCurrentAmount() == null ||
                                    campaign.getCurrentAmount().compareTo(newCurrentAmount) != 0)) {
                        campaign.setCurrentAmount(newCurrentAmount);
                        hasChanges = true;
                        logger.info("Updated current amount for campaign ID {}: {}",
                                campaign.getCampaignId(), newCurrentAmount);
                    }
                }

                if (scrapedData.containsKey("goal")) {
                    BigDecimal newGoalAmount = parseAmountString(scrapedData.get("goal"));
                    if (newGoalAmount != null &&
                            (campaign.getGoalAmount() == null ||
                                    campaign.getGoalAmount().compareTo(newGoalAmount) != 0)) {
                        campaign.setGoalAmount(newGoalAmount);
                        hasChanges = true;
                        logger.info("Updated goal amount for campaign ID {}: {}",
                                campaign.getCampaignId(), newGoalAmount);
                    }
                }

                if (hasChanges) {
                    campaignRepository.save(campaign);
                    logger.debug("Saved updated campaign ID: {}", campaign.getCampaignId());
                    return true;
                }
            }

            logger.debug("No changes detected for campaign ID: {}", campaign.getCampaignId());
            return false;

        } catch (UserValidationException e) {
            logger.error("Validation failed for campaign ID {}: {}",
                    campaign.getCampaignId(), e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error scraping data for campaign ID {} with URL {}: {}",
                    campaign.getCampaignId(), campaign.getBankaUrl(), e.getMessage());
            throw new RuntimeException("Failed to update campaign amounts due to unexpected error", e);
        }
    }

    public boolean updateCampaignById(Long campaignId) {
        logger.info("Attempting to update campaign by ID: {}", campaignId);

        try {
            Campaign campaign = campaignRepository.findById(campaignId)
                    .orElseThrow(() -> {
                        logger.warn("Campaign with ID {} not found", campaignId);
                        return new UserNotFoundException("Campaign with ID " + campaignId + " not found");
                    });

            return updateCampaignAmounts(campaign);

        } catch (UserNotFoundException e) {
            logger.error("Error updating campaign by ID {}: {}", campaignId, e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error updating campaign ID {}: {}", campaignId, e.getMessage());
            throw new RuntimeException("Failed to update campaign due to unexpected error", e);
        }
    }

    private void validateBankaUrl(String bankaUrl) {
        if (!StringUtils.hasText(bankaUrl)) {
            logger.warn("Validation failed: Banka URL is required");
            throw new UserValidationException("Banka URL is required");
        }

        if (!isValidUrl(bankaUrl)) {
            logger.warn("Validation failed: Invalid banka URL format: {}", bankaUrl);
            throw new UserValidationException("Invalid banka URL format: " + bankaUrl);
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

    private BigDecimal parseAmountString(String amountStr) {
        if (!StringUtils.hasText(amountStr)) {
            logger.warn("Amount string is empty or null");
            return null;
        }

        try {
            Pattern pattern = Pattern.compile("([\\d\\s,\\.]+)");
            Matcher matcher = pattern.matcher(amountStr);

            if (matcher.find()) {
                String cleanedAmount = matcher.group(1)
                        .replaceAll("\\s", "")
                        .replace(",", ".");
                BigDecimal amount = new BigDecimal(cleanedAmount);
                logger.debug("Parsed amount: {} from string: {}", amount, amountStr);
                return amount;
            } else {
                logger.warn("No valid amount found in string: {}", amountStr);
                return null;
            }
        } catch (Exception e) {
            logger.warn("Failed to parse amount string: {} - {}", amountStr, e.getMessage());
            return null;
        }
    }
}