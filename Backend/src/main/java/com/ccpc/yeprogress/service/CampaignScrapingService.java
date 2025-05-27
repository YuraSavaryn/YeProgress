package com.ccpc.yeprogress.service;

import com.ccpc.yeprogress.model.Campaign;
import com.ccpc.yeprogress.repository.CampaignRepository;
import com.ccpc.yeprogress.scraper.MonobankJarScraper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.logging.Logger;

@Service
public class CampaignScrapingService {

    private static final Logger logger = Logger.getLogger(CampaignScrapingService.class.getName());

    @Autowired
    private CampaignRepository campaignRepository;

    // Запускається кожні 30 хвилин
    @Scheduled(fixedRate = 180000) // 3 хвилини в мілісекундах
    @Transactional
    public void updateAllCampaignAmounts() {
        logger.info("Починаємо оновлення сум для всіх кампаній");

        List<Campaign> campaigns = campaignRepository.findAll();
        int updatedCount = 0;

        for (Campaign campaign : campaigns) {
            if (campaign.getBankaUrl() != null && !campaign.getBankaUrl().isEmpty()) {
                try {
                    boolean updated = updateCampaignAmounts(campaign);
                    if (updated) {
                        updatedCount++;
                    }
                    // Додаємо паузу між запитами, щоб не перевантажувати сервер
                    Thread.sleep(2000);
                } catch (Exception e) {
                    logger.warning("Помилка при оновленні кампанії ID " + campaign.getCampaignId() + ": " + e.getMessage());
                }
            }
        }

        logger.info("Оновлено сум для " + updatedCount + " кампаній з " + campaigns.size());
    }

    // Метод для оновлення конкретної кампанії
    @Transactional
    public boolean updateCampaignAmounts(Campaign campaign) {
        try {
            Map<String, String> scrapedData = MonobankJarScraper.getJarAmounts(campaign.getBankaUrl());

            if (scrapedData != null) {
                boolean hasChanges = false;

                // Оновлюємо поточну суму
                if (scrapedData.containsKey("accumulated")) {
                    BigDecimal newCurrentAmount = parseAmountString(scrapedData.get("accumulated"));
                    if (newCurrentAmount != null &&
                            (campaign.getCurrentAmount() == null ||
                                    campaign.getCurrentAmount().compareTo(newCurrentAmount) != 0)) {

                        campaign.setCurrentAmount(newCurrentAmount);
                        hasChanges = true;
                        logger.info("Оновлено поточну суму для кампанії ID " + campaign.getCampaignId() +
                                ": " + newCurrentAmount);
                    }
                }

                // Оновлюємо цільову суму
                if (scrapedData.containsKey("goal")) {
                    BigDecimal newGoalAmount = parseAmountString(scrapedData.get("goal"));
                    if (newGoalAmount != null &&
                            (campaign.getGoalAmount() == null ||
                                    campaign.getGoalAmount().compareTo(newGoalAmount) != 0)) {

                        campaign.setGoalAmount(newGoalAmount);
                        hasChanges = true;
                        logger.info("Оновлено цільову суму для кампанії ID " + campaign.getCampaignId() +
                                ": " + newGoalAmount);
                    }
                }

                if (hasChanges) {
                    campaignRepository.save(campaign);
                    return true;
                }
            }
        } catch (Exception e) {
            logger.warning("Помилка при скрапінгу даних для кампанії ID " + campaign.getCampaignId() +
                    " з URL " + campaign.getBankaUrl() + ": " + e.getMessage());
        }

        return false;
    }

    // Метод для оновлення конкретної кампанії за ID
    public boolean updateCampaignById(Long campaignId) {
        try {
            Campaign campaign = campaignRepository.findById(campaignId).orElse(null);
            if (campaign != null) {
                return updateCampaignAmounts(campaign);
            }
        } catch (Exception e) {
            logger.warning("Помилка при оновленні кампанії ID " + campaignId + ": " + e.getMessage());
        }
        return false;
    }

    // Допоміжний метод для парсингу суми з рядка
    private BigDecimal parseAmountString(String amountStr) {
        if (amountStr == null || amountStr.isEmpty()) {
            return null;
        }

        try {
            // Видаляємо всі символи крім цифр, крапок та коми
            Pattern pattern = Pattern.compile("([\\d\\s,\\.]+)");
            Matcher matcher = pattern.matcher(amountStr);

            if (matcher.find()) {
                String cleanedAmount = matcher.group(1)
                        .replaceAll("\\s", "") // видаляємо пробіли
                        .replace(",", "."); // замінюємо кому на крапку

                return new BigDecimal(cleanedAmount);
            }
        } catch (Exception e) {
            logger.warning("Не вдалося парсити суму: " + amountStr + " - " + e.getMessage());
        }

        return null;
    }
}