package com.ccpc.yeprogress.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LoggerService {

    public static Logger getLogger(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }

    public static void logInfo(Logger logger, String message, Object... args) {
        logger.info(message, args);
    }

    public static void logDebug(Logger logger, String message, Object... args) {
        logger.debug(message, args);
    }

    public static void logWarn(Logger logger, String message, Object... args) {
        logger.warn(message, args);
    }

    public static void logError(Logger logger, String message, Object... args) {
        logger.error(message, args);
    }

    public static void logError(Logger logger, String message, Throwable throwable, Object... args) {
        logger.error(message, throwable, args);
    }

    // Спеціальні методи для логування операцій CRUD
    public static void logCreateAttempt(Logger logger, String entityType, Object identifier) {
        logger.info("Attempting to create {} with identifier: {}", entityType, identifier);
    }

    public static void logCreateSuccess(Logger logger, String entityType, Object identifier) {
        logger.info("Successfully created {} with ID: {}", entityType, identifier);
    }

    public static void logRetrieveAttempt(Logger logger, String entityType, Object identifier) {
        logger.info("Retrieving {} with identifier: {}", entityType, identifier);
    }

    public static void logRetrieveSuccess(Logger logger, String entityType, Object identifier) {
        logger.debug("Successfully retrieved {} with identifier: {}", entityType, identifier);
    }

    public static void logUpdateAttempt(Logger logger, String entityType, Object identifier) {
        logger.info("Attempting to update {} with identifier: {}", entityType, identifier);
    }

    public static void logUpdateSuccess(Logger logger, String entityType, Object identifier) {
        logger.info("Successfully updated {} with identifier: {}", entityType, identifier);
    }

    public static void logDeleteAttempt(Logger logger, String entityType, Object identifier) {
        logger.info("Attempting to delete {} with identifier: {}", entityType, identifier);
    }

    public static void logDeleteSuccess(Logger logger, String entityType, Object identifier) {
        logger.info("Successfully deleted {} with identifier: {}", entityType, identifier);
    }

    public static void logValidationStart(Logger logger, String entityType) {
        logger.debug("Validating {} DTO", entityType);
    }

    public static void logValidationFailure(Logger logger, String message, Object... args) {
        logger.warn("Validation failed: " + message, args);
    }

    public static void logEntityNotFound(Logger logger, String entityType, Object identifier) {
        logger.warn("{} with identifier {} not found", entityType, identifier);
    }

    public static void logUnexpectedError(Logger logger, String operation, String message, Throwable throwable) {
        logger.error("Unexpected error during {}: {}", operation, message, throwable);
    }

    public static void logEmailAttempt(Logger logger, String email, String subject) {
        logger.info("Attempting to send contact email from: {} with subject: {}", email, subject);
    }

    public static void logEmailSuccess(Logger logger, String subject) {
        logger.info("Successfully sent contact email with subject: {}", subject);
    }
}