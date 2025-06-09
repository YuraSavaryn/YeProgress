package com.ccpc.yeprogress.validation;

import com.ccpc.yeprogress.dto.CommentsUserDTO;
import com.ccpc.yeprogress.dto.CommentsCampaignDTO;
import com.ccpc.yeprogress.dto.ContactRequestDTO;
import com.ccpc.yeprogress.dto.SocialNetworkDTO;
import com.ccpc.yeprogress.dto.UserDTO;
import com.ccpc.yeprogress.dto.CategoryDTO;
import com.ccpc.yeprogress.dto.CampaignDTO;
import com.ccpc.yeprogress.dto.CampaignImagesDTO;
import com.ccpc.yeprogress.dto.AuthenticationDTO;
import com.ccpc.yeprogress.dto.AuthenticationMethodDTO;
import com.ccpc.yeprogress.exception.UserValidationException;
import com.ccpc.yeprogress.logger.LoggerService;
import com.ccpc.yeprogress.model.types.AuthenticationStatusType;
import com.ccpc.yeprogress.model.types.CampaignStatusType;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.net.URL;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ValidationService {

    private static final Logger logger = LoggerService.getLogger(ValidationService.class);

    private final Validator validator;

    // Constants for validation limits
    private static final int MAX_COMMENT_CONTENT_LENGTH = 500;
    private static final int MAX_SOCIAL_URL_LENGTH = 255;
    private static final int MAX_USER_NAME_LENGTH = 50;
    private static final int MAX_USER_SURNAME_LENGTH = 50;
    private static final int MAX_USER_PHONE_LENGTH = 20;
    private static final int MAX_USER_EMAIL_LENGTH = 100;
    private static final int MAX_USER_LOCATION_LENGTH = 100;
    private static final int MAX_USER_DESCRIPTION_LENGTH = 500;
    private static final int MAX_USER_IMG_URL_LENGTH = 255;
    private static final int MAX_CONTACT_SUBJECT_LENGTH = 100;
    private static final int MAX_CATEGORY_NAME_LENGTH = 50;
    private static final int MAX_CAMPAIGN_TITLE_LENGTH = 100;
    private static final int MAX_CAMPAIGN_DESCRIPTION_LENGTH = 500;
    private static final int MAX_AUTH_METHOD_NAME_LENGTH = 50;
    private static final int MAX_EXTERNAL_AUTH_ID_LENGTH = 100;

    @Autowired
    public ValidationService(Validator validator) {
        this.validator = validator;
    }

    // Authentication Method validation methods
    public void validateAuthenticationMethodDTO(AuthenticationMethodDTO authenticationMethodDTO) {
        LoggerService.logValidationStart(logger, "AuthenticationMethod");

        if (!StringUtils.hasText(authenticationMethodDTO.getAuthMethodName())) {
            LoggerService.logValidationFailure(logger, "Authentication method name is required");
            throw new UserValidationException("Authentication method name is required");
        }

        if (authenticationMethodDTO.getAuthMethodName().length() > MAX_AUTH_METHOD_NAME_LENGTH) {
            LoggerService.logValidationFailure(logger, "Authentication method name exceeds {} characters", MAX_AUTH_METHOD_NAME_LENGTH);
            throw new UserValidationException("Authentication method name must not exceed " + MAX_AUTH_METHOD_NAME_LENGTH + " characters");
        }
    }

    // Authentication validation methods
    public void validateAuthenticationDTO(AuthenticationDTO authenticationDTO) {
        LoggerService.logValidationStart(logger, "Authentication");

        if (!StringUtils.hasText(authenticationDTO.getMethodName())) {
            LoggerService.logValidationFailure(logger, "Authentication method name is required");
            throw new UserValidationException("Authentication method name is required");
        }

        if (authenticationDTO.getMethodName().length() > MAX_AUTH_METHOD_NAME_LENGTH) {
            LoggerService.logValidationFailure(logger, "Authentication method name exceeds {} characters", MAX_AUTH_METHOD_NAME_LENGTH);
            throw new UserValidationException("Authentication method name must not exceed " + MAX_AUTH_METHOD_NAME_LENGTH + " characters");
        }

        if (StringUtils.hasText(authenticationDTO.getStatusName())) {
            try {
                AuthenticationStatusType.valueOf(authenticationDTO.getStatusName());
            } catch (IllegalArgumentException e) {
                LoggerService.logValidationFailure(logger, "Invalid authentication status: {}", authenticationDTO.getStatusName());
                throw new UserValidationException("Invalid authentication status: " + authenticationDTO.getStatusName());
            }
        }

        if (StringUtils.hasText(authenticationDTO.getExternalAuthId()) &&
                authenticationDTO.getExternalAuthId().length() > MAX_EXTERNAL_AUTH_ID_LENGTH) {
            LoggerService.logValidationFailure(logger, "External auth ID exceeds {} characters", MAX_EXTERNAL_AUTH_ID_LENGTH);
            throw new UserValidationException("External auth ID must not exceed " + MAX_EXTERNAL_AUTH_ID_LENGTH + " characters");
        }
    }

    // User validation methods
    public void validateUserDTO(UserDTO userDTO, boolean isCreate) {
        LoggerService.logValidationStart(logger, "User");

        if (isCreate && !StringUtils.hasText(userDTO.getFirebaseId())) {
            LoggerService.logValidationFailure(logger, "Firebase ID is required");
            throw new UserValidationException("Firebase ID is required");
        }

        validateUserName(userDTO.getName());
        validateUserSurname(userDTO.getSurname());
        validateUserPhone(userDTO.getPhone());
        validateUserEmail(userDTO.getEmail());
        validateUserLocation(userDTO.getLocation());
        validateUserDescription(userDTO.getDescription());
        validateUserImageUrl(userDTO.getImgUrl());
    }

    private void validateUserName(String name) {
        if (StringUtils.hasText(name)) {
            if (name.length() > MAX_USER_NAME_LENGTH) {
                LoggerService.logValidationFailure(logger, "Name exceeds {} characters", MAX_USER_NAME_LENGTH);
                throw new UserValidationException("Name must not exceed " + MAX_USER_NAME_LENGTH + " characters");
            }
            if (!name.matches("^[a-zA-Zа-яА-ЯіІїЇєЄ\\s-']+$")) {
                LoggerService.logValidationFailure(logger, "Name contains invalid characters");
                throw new UserValidationException("Name contains invalid characters");
            }
        }
    }

    private void validateUserSurname(String surname) {
        if (StringUtils.hasText(surname)) {
            if (surname.length() > MAX_USER_SURNAME_LENGTH) {
                LoggerService.logValidationFailure(logger, "Surname exceeds {} characters", MAX_USER_SURNAME_LENGTH);
                throw new UserValidationException("Surname must not exceed " + MAX_USER_SURNAME_LENGTH + " characters");
            }
            if (!surname.matches("^[a-zA-Zа-яА-ЯіІїЇєЄ\\s-']+$")) {
                LoggerService.logValidationFailure(logger, "Surname contains invalid characters");
                throw new UserValidationException("Surname contains invalid characters");
            }
        }
    }

    private void validateUserPhone(String phone) {
        if (StringUtils.hasText(phone)) {
            if (phone.length() > MAX_USER_PHONE_LENGTH) {
                LoggerService.logValidationFailure(logger, "Phone exceeds {} characters", MAX_USER_PHONE_LENGTH);
                throw new UserValidationException("Phone must not exceed " + MAX_USER_PHONE_LENGTH + " characters");
            }
            if (!phone.matches("^\\+?[0-9\\s-]+$")) {
                LoggerService.logValidationFailure(logger, "Invalid phone format");
                throw new UserValidationException("Invalid phone format");
            }
        }
    }

    private void validateUserEmail(String email) {
        if (StringUtils.hasText(email)) {
            if (email.length() > MAX_USER_EMAIL_LENGTH) {
                LoggerService.logValidationFailure(logger, "Email exceeds {} characters", MAX_USER_EMAIL_LENGTH);
                throw new UserValidationException("Email must not exceed " + MAX_USER_EMAIL_LENGTH + " characters");
            }
            if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
                LoggerService.logValidationFailure(logger, "Invalid email format");
                throw new UserValidationException("Invalid email format");
            }
        }
    }

    private void validateUserLocation(String location) {
        if (StringUtils.hasText(location)) {
            if (location.length() > MAX_USER_LOCATION_LENGTH) {
                LoggerService.logValidationFailure(logger, "Location exceeds {} characters", MAX_USER_LOCATION_LENGTH);
                throw new UserValidationException("Location must not exceed " + MAX_USER_LOCATION_LENGTH + " characters");
            }
        }
    }

    private void validateUserDescription(String description) {
        if (StringUtils.hasText(description)) {
            if (description.length() > MAX_USER_DESCRIPTION_LENGTH) {
                LoggerService.logValidationFailure(logger, "Description exceeds {} characters", MAX_USER_DESCRIPTION_LENGTH);
                throw new UserValidationException("Description must not exceed " + MAX_USER_DESCRIPTION_LENGTH + " characters");
            }
        }
    }

    private void validateUserImageUrl(String imgUrl) {
        if (StringUtils.hasText(imgUrl) && !isValidUrl(imgUrl)) {
            LoggerService.logValidationFailure(logger, "Invalid image URL format: {}", imgUrl);
            throw new UserValidationException("Invalid image URL format");
        }
    }

    // Comment validation methods
    public void validateCommentDTO(CommentsUserDTO commentsUserDTO) {
        LoggerService.logValidationStart(logger, "Comment");

        if (commentsUserDTO.getUserId() == null) {
            LoggerService.logValidationFailure(logger, "User ID is required");
            throw new UserValidationException("User ID is required");
        }

        if (commentsUserDTO.getUserAuthorId() == null) {
            LoggerService.logValidationFailure(logger, "User author ID is required");
            throw new UserValidationException("User author ID is required");
        }

        if (!StringUtils.hasText(commentsUserDTO.getContent())) {
            LoggerService.logValidationFailure(logger, "Comment content is required");
            throw new UserValidationException("Comment content is required");
        }

        if (commentsUserDTO.getContent().length() > MAX_COMMENT_CONTENT_LENGTH) {
            LoggerService.logValidationFailure(logger, "Comment content exceeds {} characters", MAX_COMMENT_CONTENT_LENGTH);
            throw new UserValidationException(
                    "Comment content must not exceed " + MAX_COMMENT_CONTENT_LENGTH + " characters");
        }
    }

    public void validateCommentDTO(CommentsCampaignDTO commentsCampaignDTO) {
        LoggerService.logValidationStart(logger, "CampaignComment");

        if (commentsCampaignDTO.getUserId() == null) {
            LoggerService.logValidationFailure(logger, "User ID is required");
            throw new UserValidationException("User ID is required");
        }

        if (commentsCampaignDTO.getCampaignId() == null) {
            LoggerService.logValidationFailure(logger, "Campaign ID is required");
            throw new UserValidationException("Campaign ID is required");
        }

        if (!StringUtils.hasText(commentsCampaignDTO.getContent())) {
            LoggerService.logValidationFailure(logger, "Comment content is required");
            throw new UserValidationException("Comment content is required");
        }

        if (commentsCampaignDTO.getContent().length() > MAX_COMMENT_CONTENT_LENGTH) {
            LoggerService.logValidationFailure(logger, "Comment content exceeds {} characters", MAX_COMMENT_CONTENT_LENGTH);
            throw new UserValidationException(
                    "Comment content must not exceed " + MAX_COMMENT_CONTENT_LENGTH + " characters");
        }
    }

    // Social Network validation methods
    public void validateSocialNetworkDTO(SocialNetworkDTO socialNetworkDTO) {
        LoggerService.logValidationStart(logger, "Social Network");

        if (socialNetworkDTO.getUserId() == null) {
            LoggerService.logValidationFailure(logger, "User ID is required");
            throw new UserValidationException("User ID is required");
        }

        if (!StringUtils.hasText(socialNetworkDTO.getSocialNetworkURL())) {
            LoggerService.logValidationFailure(logger, "Social network URL is required");
            throw new UserValidationException("Social network URL is required");
        }

        if (socialNetworkDTO.getSocialNetworkURL().length() > MAX_SOCIAL_URL_LENGTH) {
            LoggerService.logValidationFailure(logger, "Social network URL exceeds {} characters", MAX_SOCIAL_URL_LENGTH);
            throw new UserValidationException(
                    "Social network URL must not exceed " + MAX_SOCIAL_URL_LENGTH + " characters");
        }

        if (!isValidUrl(socialNetworkDTO.getSocialNetworkURL())) {
            LoggerService.logValidationFailure(logger, "Invalid social network URL format: {}",
                    socialNetworkDTO.getSocialNetworkURL());
            throw new UserValidationException("Invalid social network URL format");
        }
    }

    // Contact Request validation methods
    public void validateContactRequestDTO(ContactRequestDTO request) {
        LoggerService.logValidationStart(logger, "Contact Request");

        Set<ConstraintViolation<ContactRequestDTO>> violations = validator.validate(request);
        if (!violations.isEmpty()) {
            String errorMessage = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining("; "));
            LoggerService.logValidationFailure(logger, errorMessage);
            throw new UserValidationException("Validation failed: " + errorMessage);
        }

        if (request.getSubject().length() > MAX_CONTACT_SUBJECT_LENGTH) {
            LoggerService.logValidationFailure(logger, "Subject exceeds {} characters", MAX_CONTACT_SUBJECT_LENGTH);
            throw new UserValidationException("Subject must not exceed " + MAX_CONTACT_SUBJECT_LENGTH + " characters");
        }
    }

    // Category validation methods
    public void validateCategoryDTO(CategoryDTO categoryDTO) {
        LoggerService.logValidationStart(logger, "Category");

        if (!StringUtils.hasText(categoryDTO.getName())) {
            LoggerService.logValidationFailure(logger, "Category name is required");
            throw new UserValidationException("Category name is required");
        }

        if (categoryDTO.getName().length() > MAX_CATEGORY_NAME_LENGTH) {
            LoggerService.logValidationFailure(logger, "Category name exceeds {} characters", MAX_CATEGORY_NAME_LENGTH);
            throw new UserValidationException(
                    "Category name must not exceed " + MAX_CATEGORY_NAME_LENGTH + " characters");
        }

        if (!categoryDTO.getName().matches("^[a-zA-Zа-яА-ЯіІїЇєЄ\\s-']+$")) {
            LoggerService.logValidationFailure(logger, "Category name contains invalid characters");
            throw new UserValidationException("Category name contains invalid characters");
        }
    }

    // Campaign validation methods
    public void validateCampaignDTO(CampaignDTO campaignDTO) {
        LoggerService.logValidationStart(logger, "Campaign");

        if (!StringUtils.hasText(campaignDTO.getTitle())) {
            LoggerService.logValidationFailure(logger, "Campaign title is required");
            throw new UserValidationException("Campaign title is required");
        }

        if (campaignDTO.getTitle().length() > MAX_CAMPAIGN_TITLE_LENGTH) {
            LoggerService.logValidationFailure(logger, "Campaign title exceeds {} characters", MAX_CAMPAIGN_TITLE_LENGTH);
            throw new UserValidationException(
                    "Campaign title must not exceed " + MAX_CAMPAIGN_TITLE_LENGTH + " characters");
        }

        if (StringUtils.hasText(campaignDTO.getDescription()) &&
                campaignDTO.getDescription().length() > MAX_CAMPAIGN_DESCRIPTION_LENGTH) {
            LoggerService.logValidationFailure(logger, "Campaign description exceeds {} characters", MAX_CAMPAIGN_DESCRIPTION_LENGTH);
            throw new UserValidationException(
                    "Campaign description must not exceed " + MAX_CAMPAIGN_DESCRIPTION_LENGTH + " characters");
        }

        if (StringUtils.hasText(campaignDTO.getBankaUrl()) && !isValidUrl(campaignDTO.getBankaUrl())) {
            LoggerService.logValidationFailure(logger, "Invalid banka URL format: {}", campaignDTO.getBankaUrl());
            throw new UserValidationException("Invalid banka URL format");
        }

        if (StringUtils.hasText(campaignDTO.getMainImgUrl()) && !isValidUrl(campaignDTO.getMainImgUrl())) {
            LoggerService.logValidationFailure(logger, "Invalid main image URL format: {}", campaignDTO.getMainImgUrl());
            throw new UserValidationException("Invalid main image URL format");
        }

        if (StringUtils.hasText(campaignDTO.getStatus())) {
            try {
                CampaignStatusType.valueOf(campaignDTO.getStatus());
            } catch (IllegalArgumentException e) {
                LoggerService.logValidationFailure(logger, "Invalid campaign status: {}", campaignDTO.getStatus());
                throw new UserValidationException("Invalid campaign status: " + campaignDTO.getStatus());
            }
        }
    }

    // Campaign Image validation methods
    public void validateCampaignImageDTO(CampaignImagesDTO campaignImagesDTO) {
        LoggerService.logValidationStart(logger, "CampaignImage");

        if (campaignImagesDTO.getCampaignId() == null) {
            LoggerService.logValidationFailure(logger, "Campaign ID is required");
            throw new UserValidationException("Campaign ID is required");
        }

        if (!StringUtils.hasText(campaignImagesDTO.getImgUrl())) {
            LoggerService.logValidationFailure(logger, "Image URL is required");
            throw new UserValidationException("Image URL is required");
        }

        if (!isValidUrl(campaignImagesDTO.getImgUrl())) {
            LoggerService.logValidationFailure(logger, "Invalid image URL format: {}", campaignImagesDTO.getImgUrl());
            throw new UserValidationException("Invalid image URL format");
        }
    }

    // Common validation methods
    public void validateFirebaseId(String firebaseId) {
        if (!StringUtils.hasText(firebaseId)) {
            LoggerService.logValidationFailure(logger, "Firebase ID is required");
            throw new UserValidationException("Firebase ID is required");
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