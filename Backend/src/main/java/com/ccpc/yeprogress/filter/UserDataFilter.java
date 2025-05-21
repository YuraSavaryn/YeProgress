// Data Filter
package com.ccpc.yeprogress.filter;

import com.ccpc.yeprogress.dto.UserDTO;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class UserDataFilter {

//    public UserDTO sanitizeInput(UserDTO UserDTO) {
//        return new UserDTO(
//                sanitizeString(UserDTO.getFirebaseId()),
//                sanitizeString(UserDTO.getName()),
//                sanitizeString(UserDTO.getSurname()),
//                sanitizePhoneNumber(UserDTO.getPhone()),
//                sanitizeEmail(UserDTO.getEmail()),
//                sanitizeString(UserDTO.getLocation    ()),
//                sanitizeString(UserDTO.getDescription()),
//                sanitizeUrl(UserDTO.getImgUrl()),
//                UserDTO.getCreatedAt(),
//                UserDTO.getsVerified()
//        );
//    }

    private String sanitizeString(String input) {
        if (!StringUtils.hasText(input)) {
            return null;
        }

        return input.trim()
                .replaceAll("\\s+", " ") // Replace multiple spaces with single space
                .replaceAll("[<>\"'&]", ""); // Remove potentially dangerous characters
    }

    private String sanitizeEmail(String email) {
        if (!StringUtils.hasText(email)) {
            return null;
        }

        return email.trim().toLowerCase();
    }

    private String sanitizePhoneNumber(String phone) {
        if (!StringUtils.hasText(phone)) {
            return null;
        }

        // Remove all non-digit characters except +
        return phone.replaceAll("[^+\\d]", "");
    }

    private String sanitizeUrl(String url) {
        if (!StringUtils.hasText(url)) {
            return null;
        }

        return url.trim();
    }
}