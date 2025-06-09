package com.ccpc.yeprogress.service;

import com.ccpc.yeprogress.dto.AuthenticationDTO;
import com.ccpc.yeprogress.exception.UserNotFoundException;
import com.ccpc.yeprogress.exception.UserValidationException;
import com.ccpc.yeprogress.mapper.AuthenticationMapper;
import com.ccpc.yeprogress.model.Authentication;
import com.ccpc.yeprogress.model.User;
import com.ccpc.yeprogress.model.types.AuthenticationStatusType;
import com.ccpc.yeprogress.repository.AuthenticationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    private final AuthenticationRepository authenticationRepository;
    private final AuthenticationMapper authenticationMapper;

    @Autowired
    public AuthenticationService(AuthenticationRepository authenticationRepository,
                                 AuthenticationMapper authenticationMapper) {
        this.authenticationRepository = authenticationRepository;
        this.authenticationMapper = authenticationMapper;
    }

    public AuthenticationDTO createAuthentication(AuthenticationDTO authenticationDTO) {
        logger.info("Attempting to create authentication for method: {}",
                authenticationDTO.getMethodName());

        try {
            // Validate input
            validateAuthenticationDTO(authenticationDTO);

            Authentication authentication = authenticationMapper.toEntity(authenticationDTO);
            Authentication savedAuthentication = authenticationRepository.save(authentication);
            logger.info("Successfully created authentication with ID: {}",
                    savedAuthentication.getAuthenticationId());

            return authenticationMapper.toDto(savedAuthentication);

        } catch (UserValidationException e) {
            logger.error("Validation failed for authentication creation: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error during authentication creation: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create authentication due to unexpected error", e);
        }
    }

    public AuthenticationDTO createAuthentication(User user) {
        logger.info("Attempting to create authentication for user ID: {}",
                user != null ? user.getUserId() : "null");

        try {
            // Validate user
            if (user == null || user.getUserId() == null) {
                logger.warn("Validation failed: User is required for authentication creation");
                throw new UserValidationException("User is required for authentication creation");
            }

            Authentication authentication = new Authentication();
            authentication.setUser(user);
            authentication.setStatus(AuthenticationStatusType.NOT_VERIFIED);
            Authentication savedAuthentication = authenticationRepository.save(authentication);
            logger.info("Successfully created authentication with ID: {} for user ID: {}",
                    savedAuthentication.getAuthenticationId(), user.getUserId());

            return authenticationMapper.toDto(savedAuthentication);

        } catch (UserValidationException e) {
            logger.error("Validation failed for authentication creation: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error during authentication creation: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create authentication due to unexpected error", e);
        }
    }

    public AuthenticationDTO getAuthenticationByUserId(Long userId) {
        logger.info("Retrieving authentication for user ID: {}", userId);

        try {
            Authentication authentication = authenticationRepository.findByUser_UserId(userId);
            if (authentication == null) {
                logger.warn("Authentication for user ID {} not found", userId);
                throw new UserNotFoundException("Authentication for user ID " + userId + " not found");
            }

            logger.debug("Successfully retrieved authentication for user ID: {}", userId);
            return authenticationMapper.toDto(authentication);

        } catch (UserNotFoundException e) {
            logger.error("Error retrieving authentication: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error retrieving authentication: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve authentication due to unexpected error", e);
        }
    }

    public List<AuthenticationDTO> getAllAuthentications() {
        logger.info("Retrieving all authentications");

        try {
            List<Authentication> authentications = authenticationRepository.findAll();
            logger.debug("Retrieved {} authentications", authentications.size());

            return authentications.stream()
                    .map(authenticationMapper::toDto)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            logger.error("Unexpected error retrieving all authentications: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve authentications due to unexpected error", e);
        }
    }

    public AuthenticationDTO updateAuthentication(Long userId, AuthenticationDTO authenticationDTO) {
        logger.info("Attempting to update authentication for user ID: {}", userId);

        try {
            // Validate input
            validateAuthenticationDTO(authenticationDTO);

            Authentication authentication = authenticationRepository.findByUser_UserId(userId);
            if (authentication == null) {
                logger.warn("Authentication for user ID {} not found", userId);
                throw new UserNotFoundException("Authentication for user ID " + userId + " not found");
            }

            authenticationMapper.updateEntityFromDto(authenticationDTO, authentication);
            Authentication savedAuthentication = authenticationRepository.save(authentication);
            logger.info("Successfully updated authentication for user ID: {}", userId);

            return authenticationMapper.toDto(savedAuthentication);

        } catch (UserValidationException | UserNotFoundException e) {
            logger.error("Error updating authentication: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error during authentication update: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update authentication due to unexpected error", e);
        }
    }

    public void deleteAuthentication(Long id) {
        logger.info("Attempting to delete authentication with ID: {}", id);

        try {
            if (!authenticationRepository.existsById(id)) {
                logger.warn("Authentication deletion failed: ID {} not found", id);
                throw new UserNotFoundException("Authentication with ID " + id + " not found");
            }

            authenticationRepository.deleteById(id);
            logger.info("Successfully deleted authentication with ID: {}", id);

        } catch (UserNotFoundException e) {
            logger.error("Error deleting authentication: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error during authentication deletion: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to delete authentication due to unexpected error", e);
        }
    }

    private void validateAuthenticationDTO(AuthenticationDTO authenticationDTO) {
        logger.debug("Validating authentication DTO");

        if (!StringUtils.hasText(authenticationDTO.getMethodName())) {
            logger.warn("Validation failed: Authentication method name is required");
            throw new UserValidationException("Authentication method name is required");
        }

        if (authenticationDTO.getMethodName().length() > 50) {
            logger.warn("Validation failed: Authentication method name exceeds 50 characters");
            throw new UserValidationException("Authentication method name must not exceed 50 characters");
        }

        if (StringUtils.hasText(authenticationDTO.getStatusName())) {
            try {
                AuthenticationStatusType.valueOf(authenticationDTO.getStatusName());
            } catch (IllegalArgumentException e) {
                logger.warn("Validation failed: Invalid authentication status: {}",
                        authenticationDTO.getStatusName());
                throw new UserValidationException("Invalid authentication status: " +
                        authenticationDTO.getStatusName());
            }
        }

        if (StringUtils.hasText(authenticationDTO.getExternalAuthId()) &&
                authenticationDTO.getExternalAuthId().length() > 100) {
            logger.warn("Validation failed: External auth ID exceeds 100 characters");
            throw new UserValidationException("External auth ID must not exceed 100 characters");
        }
    }
}