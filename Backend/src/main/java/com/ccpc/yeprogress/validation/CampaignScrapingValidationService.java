package com.ccpc.yeprogress.validation;

import com.ccpc.yeprogress.exception.UserValidationException;
import com.ccpc.yeprogress.logger.LoggerService;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CampaignScrapingValidationService {

    private static final Logger logger = LoggerService.getLogger(CampaignScrapingValidationService.class);

    public void validateBankaUrl(String bankaUrl) {
        LoggerService.logValidationStart(logger, "Banka URL");

        if (!StringUtils.hasText(bankaUrl)) {
            LoggerService.logValidationFailure(logger, "Banka URL is required");
            throw new UserValidationException("Banka URL is required");
        }

        if (!isValidUrl(bankaUrl)) {
            LoggerService.logValidationFailure(logger, "Invalid banka URL format: {}", bankaUrl);
            throw new UserValidationException("Invalid banka URL format: " + bankaUrl);
        }
    }

    public BigDecimal parseAmountString(String amountStr) {
        LoggerService.logValidationStart(logger, "Amount String");

        if (!StringUtils.hasText(amountStr)) {
            LoggerService.logValidationFailure(logger, "Amount string is empty or null");
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
                LoggerService.logDebug(logger, "Parsed amount: {} from string: {}", amount, amountStr);
                return amount;
            } else {
                LoggerService.logValidationFailure(logger, "No valid amount found in string: {}", amountStr);
                return null;
            }
        } catch (Exception e) {
            LoggerService.logValidationFailure(logger, "Failed to parse amount string: {} - {}", amountStr, e.getMessage());
            return null;
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