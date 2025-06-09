package com.ccpc.yeprogress.service;

import com.ccpc.yeprogress.dto.AuthenticationMethodDTO;
import com.ccpc.yeprogress.exception.UserNotFoundException;
import com.ccpc.yeprogress.exception.UserValidationException;
import com.ccpc.yeprogress.logger.LoggerService;
import com.ccpc.yeprogress.mapper.AuthenticationMethodMapper;
import com.ccpc.yeprogress.model.AuthenticationMethod;
import com.ccpc.yeprogress.repository.AuthenticationMethodRepository;
import com.ccpc.yeprogress.validation.ValidationService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthenticationMethodService {

    private static final Logger logger = LoggerService.getLogger(AuthenticationMethodService.class);

    private final AuthenticationMethodRepository authenticationMethodRepository;
    private final AuthenticationMethodMapper authenticationMethodMapper;
    private final ValidationService validationService;

    @Autowired
    public AuthenticationMethodService(AuthenticationMethodRepository authenticationMethodRepository,
                                       AuthenticationMethodMapper authenticationMethodMapper,
                                       ValidationService validationService) {
        this.authenticationMethodRepository = authenticationMethodRepository;
        this.authenticationMethodMapper = authenticationMethodMapper;
        this.validationService = validationService;
    }

    @Transactional
    public AuthenticationMethodDTO createAuthenticationMethod(AuthenticationMethodDTO authenticationMethodDTO) {
        LoggerService.logCreateAttempt(logger, "AuthenticationMethod", authenticationMethodDTO.getAuthMethodName());

        try {
            validationService.validateAuthenticationMethodDTO(authenticationMethodDTO);

            AuthenticationMethod authenticationMethod = authenticationMethodMapper.toEntity(authenticationMethodDTO);
            AuthenticationMethod savedAuthenticationMethod = authenticationMethodRepository.save(authenticationMethod);
            LoggerService.logCreateSuccess(logger, "AuthenticationMethod", savedAuthenticationMethod.getAuthMethodId());

            return authenticationMethodMapper.toDto(savedAuthenticationMethod);

        } catch (UserValidationException e) {
            LoggerService.logError(logger, "Validation failed for authentication method creation: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            LoggerService.logUnexpectedError(logger, "authentication method creation", e.getMessage(), e);
            throw new RuntimeException("Failed to create authentication method due to unexpected error", e);
        }
    }

    public AuthenticationMethodDTO getAuthenticationMethodById(Long id) {
        LoggerService.logRetrieveAttempt(logger, "AuthenticationMethod", id);

        try {
            AuthenticationMethod authenticationMethod = authenticationMethodRepository.findById(id)
                    .orElseThrow(() -> {
                        LoggerService.logEntityNotFound(logger, "AuthenticationMethod", id);
                        return new UserNotFoundException("Authentication method with ID " + id + " not found");
                    });

            LoggerService.logRetrieveSuccess(logger, "AuthenticationMethod", id);
            return authenticationMethodMapper.toDto(authenticationMethod);

        } catch (UserNotFoundException e) {
            LoggerService.logError(logger, "Error retrieving authentication method: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            LoggerService.logUnexpectedError(logger, "authentication method retrieval", e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve authentication method due to unexpected error", e);
        }
    }

    public List<AuthenticationMethodDTO> getAllAuthenticationMethods() {
        LoggerService.logRetrieveAttempt(logger, "All AuthenticationMethods", "all");

        try {
            List<AuthenticationMethod> methods = authenticationMethodRepository.findAll();
            LoggerService.logRetrieveSuccess(logger, "All AuthenticationMethods", methods.size());

            return methods.stream()
                    .map(authenticationMethodMapper::toDto)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            LoggerService.logUnexpectedError(logger, "retrieval of all authentication methods", e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve authentication methods due to unexpected error", e);
        }
    }

    @Transactional
    public AuthenticationMethodDTO updateAuthenticationMethod(Long id, AuthenticationMethodDTO authenticationMethodDTO) {
        LoggerService.logUpdateAttempt(logger, "AuthenticationMethod", id);

        try {
            validationService.validateAuthenticationMethodDTO(authenticationMethodDTO);

            AuthenticationMethod authenticationMethod = authenticationMethodRepository.findById(id)
                    .orElseThrow(() -> {
                        LoggerService.logEntityNotFound(logger, "AuthenticationMethod", id);
                        return new UserNotFoundException("Authentication method with ID " + id + " not found");
                    });

            authenticationMethodMapper.updateEntityFromDto(authenticationMethodDTO, authenticationMethod);
            AuthenticationMethod savedAuthenticationMethod = authenticationMethodRepository.save(authenticationMethod);
            LoggerService.logUpdateSuccess(logger, "AuthenticationMethod", id);

            return authenticationMethodMapper.toDto(savedAuthenticationMethod);

        } catch (UserValidationException | UserNotFoundException e) {
            LoggerService.logError(logger, "Error updating authentication method: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            LoggerService.logUnexpectedError(logger, "authentication method update", e.getMessage(), e);
            throw new RuntimeException("Failed to update authentication method due to unexpected error", e);
        }
    }

    @Transactional
    public void deleteAuthenticationMethod(Long id) {
        LoggerService.logDeleteAttempt(logger, "AuthenticationMethod", id);

        try {
            if (!authenticationMethodRepository.existsById(id)) {
                LoggerService.logEntityNotFound(logger, "AuthenticationMethod", id);
                throw new UserNotFoundException("Authentication method with ID " + id + " not found");
            }

            authenticationMethodRepository.deleteById(id);
            LoggerService.logDeleteSuccess(logger, "AuthenticationMethod", id);

        } catch (UserNotFoundException e) {
            LoggerService.logError(logger, "Error deleting authentication method: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            LoggerService.logUnexpectedError(logger, "authentication method deletion", e.getMessage(), e);
            throw new RuntimeException("Failed to delete authentication method due to unexpected error", e);
        }
    }
}