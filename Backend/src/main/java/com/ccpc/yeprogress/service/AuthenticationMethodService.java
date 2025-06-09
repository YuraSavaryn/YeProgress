package com.ccpc.yeprogress.service;

import com.ccpc.yeprogress.dto.AuthenticationMethodDTO;
import com.ccpc.yeprogress.exception.UserNotFoundException;
import com.ccpc.yeprogress.exception.UserValidationException;
import com.ccpc.yeprogress.mapper.AuthenticationMethodMapper;
import com.ccpc.yeprogress.model.AuthenticationMethod;
import com.ccpc.yeprogress.repository.AuthenticationMethodRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthenticationMethodService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationMethodService.class);

    private final AuthenticationMethodRepository authenticationMethodRepository;
    private final AuthenticationMethodMapper authenticationMethodMapper;

    @Autowired
    public AuthenticationMethodService(AuthenticationMethodRepository authenticationMethodRepository,
                                       AuthenticationMethodMapper authenticationMethodMapper) {
        this.authenticationMethodRepository = authenticationMethodRepository;
        this.authenticationMethodMapper = authenticationMethodMapper;
    }

    public AuthenticationMethodDTO createAuthenticationMethod(AuthenticationMethodDTO authenticationMethodDTO) {
        logger.info("Attempting to create authentication method: {}", authenticationMethodDTO.getAuthMethodName());

        try {
            // Validate input
            validateAuthenticationMethod(authenticationMethodDTO);

            AuthenticationMethod authenticationMethod = authenticationMethodMapper.toEntity(authenticationMethodDTO);
            AuthenticationMethod savedAuthenticationMethod = authenticationMethodRepository.save(authenticationMethod);
            logger.info("Successfully created authentication method with ID: {}",
                    savedAuthenticationMethod.getAuthMethodId());

            return authenticationMethodMapper.toDto(savedAuthenticationMethod);

        } catch (UserValidationException e) {
            logger.error("Validation failed for authentication method creation: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error during authentication method creation: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create authentication method due to unexpected error", e);
        }
    }

    public AuthenticationMethodDTO getAuthenticationMethodById(Long id) {
        logger.info("Retrieving authentication method with ID: {}", id);

        try {
            AuthenticationMethod authenticationMethod = authenticationMethodRepository.findById(id)
                    .orElseThrow(() -> {
                        logger.warn("Authentication method with ID {} not found", id);
                        return new UserNotFoundException("Authentication method with ID " + id + " not found");
                    });

            logger.debug("Successfully retrieved authentication method with ID: {}", id);
            return authenticationMethodMapper.toDto(authenticationMethod);

        } catch (UserNotFoundException e) {
            logger.error("Error retrieving authentication method: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error retrieving authentication method: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve authentication method due to unexpected error", e);
        }
    }


    public List<AuthenticationMethodDTO> getAllAuthenticationMethods() {
        logger.info("Retrieving all authentication methods");

        try {
            List<AuthenticationMethod> methods = authenticationMethodRepository.findAll();
            logger.debug("Retrieved {} authentication methods", methods.size());

            return methods.stream()
                    .map(authenticationMethodMapper::toDto)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            logger.error("Unexpected error retrieving all authentication methods: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve authentication methods due to unexpected error", e);
        }
    }

    public AuthenticationMethodDTO updateAuthenticationMethod(Long id, AuthenticationMethodDTO authenticationMethodDTO) {
        logger.info("Attempting to update authentication method with ID: {}", id);

        try {
            // Validate input
            validateAuthenticationMethod(authenticationMethodDTO);

            AuthenticationMethod authenticationMethod = authenticationMethodRepository.findById(id)
                    .orElseThrow(() -> {
                        logger.warn("Authentication method with ID {} not found", id);
                        return new UserNotFoundException("Authentication method with ID " + id + " not found");
                    });

            authenticationMethodMapper.updateEntityFromDto(authenticationMethodDTO, authenticationMethod);
            AuthenticationMethod savedAuthenticationMethod = authenticationMethodRepository.save(authenticationMethod);
            logger.info("Successfully updated authentication method with ID: {}", id);

            return authenticationMethodMapper.toDto(savedAuthenticationMethod);

        } catch (UserValidationException | UserNotFoundException e) {
            logger.error("Error updating authentication method: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error during authentication method update: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update authentication method due to unexpected error", e);
        }
    }


    public void deleteAuthenticationMethod(Long id) {
        logger.info("Attempting to delete authentication method with ID: {}", id);

        try {
            if (!authenticationMethodRepository.existsById(id)) {
                logger.warn("Authentication method deletion failed: ID {} not found", id);
                throw new UserNotFoundException("Authentication method with ID " + id + " not found");
            }

            authenticationMethodRepository.deleteById(id);
            logger.info("Successfully deleted authentication method with ID: {}", id);

        } catch (UserNotFoundException e) {
            logger.error("Error deleting authentication method: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error during authentication method deletion: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to delete authentication method due to unexpected error", e);
        }
    }


    private void validateAuthenticationMethod(AuthenticationMethodDTO authenticationMethodDTO) {
        logger.debug("Validating authentication method DTO");

        if (!StringUtils.hasText(authenticationMethodDTO.getAuthMethodName())) {
            logger.warn("Validation failed: Authentication method name is required");
            throw new UserValidationException("Authentication method name is required");
        }

        if (authenticationMethodDTO.getAuthMethodName().length() > 50) {
            logger.warn("Validation failed: Authentication method name exceeds 50 characters");
            throw new UserValidationException("Authentication method name must not exceed 50 characters");
        }
    }
}