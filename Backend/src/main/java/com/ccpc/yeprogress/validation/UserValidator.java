package com.ccpc.yeprogress.validation;

import com.ccpc.yeprogress.dto.UserDTO;
import com.ccpc.yeprogress.exception.UserValidationException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class UserValidator {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^\\+?[1-9]\\d{1,14}$");

    private static final int MAX_NAME_LENGTH = 50;
    private static final int MAX_DESCRIPTION_LENGTH = 500;
    private static final int MAX_LOCATION_LENGTH = 100;

    public void validateUserForCreation(UserDTO UserDTO) {
        List<String> errors = new ArrayList<>();

        // Required fields validation
        if (!StringUtils.hasText(UserDTO.getFirebaseId())) {
            errors.add("Firebase ID is required");
        }

        if (!StringUtils.hasText(UserDTO.getName())) {
            errors.add("Name is required");
        }

        if (!StringUtils.hasText(UserDTO.getEmail())) {
            errors.add("Email is required");
        }

        // Common validation
        validateCommonFields(UserDTO, errors);

        if (!errors.isEmpty()) {
            throw new UserValidationException("Validation failed: " + String.join(", ", errors));
        }
    }

    public void validateUserForUpdate(UserDTO UserDTO) {
        List<String> errors = new ArrayList<>();

        // For update, only validate provided fields
        validateCommonFields(UserDTO, errors);

        if (!errors.isEmpty()) {
            throw new UserValidationException("Validation failed: " + String.join(", ", errors));
        }
    }

    private void validateCommonFields(UserDTO UserDTO, List<String> errors) {
        // Name validation
        if (StringUtils.hasText(UserDTO.getName())) {
            if (UserDTO.getName().length() > MAX_NAME_LENGTH) {
                errors.add("Name must not exceed " + MAX_NAME_LENGTH + " characters");
            }
            if (!UserDTO.getName().matches("^[a-zA-Zа-яА-ЯіІїЇєЄ\\s-']+$")) {
                errors.add("Name contains invalid characters");
            }
        }

        // Surname validation
        if (StringUtils.hasText(UserDTO.getSurname())) {
            if (UserDTO.getSurname().length() > MAX_NAME_LENGTH) {
                errors.add("Surname must not exceed " + MAX_NAME_LENGTH + " characters");
            }
            if (!UserDTO.getSurname().matches("^[a-zA-Zа-яА-ЯіІїЇєЄ\\s-']+$")) {
                errors.add("Surname contains invalid characters");
            }
        }

        // Email validation
        if (StringUtils.hasText(UserDTO.getEmail())) {
            if (!EMAIL_PATTERN.matcher(UserDTO.getEmail()).matches()) {
                errors.add("Invalid email format");
            }
        }

        // Phone validation
        if (StringUtils.hasText(UserDTO.getPhone())) {
            if (!PHONE_PATTERN.matcher(UserDTO.getPhone()).matches()) {
                errors.add("Invalid phone format");
            }
        }

        // Location validation
        if (StringUtils.hasText(UserDTO.getLocation())) {
            if (UserDTO.getLocation().length() > MAX_LOCATION_LENGTH) {
                errors.add("Location must not exceed " + MAX_LOCATION_LENGTH + " characters");
            }
        }

        // Description validation
        if (StringUtils.hasText(UserDTO.getDescription())) {
            if (UserDTO.getDescription().length() > MAX_DESCRIPTION_LENGTH) {
                errors.add("Description must not exceed " + MAX_DESCRIPTION_LENGTH + " characters");
            }
        }

        // Image URL validation
        if (StringUtils.hasText(UserDTO.getImgUrl())) {
            if (!isValidUrl(UserDTO.getImgUrl())) {
                errors.add("Invalid image URL format");
            }
        }
    }

    private boolean isValidUrl(String url) {
        try {
            new java.net.URL(url);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}