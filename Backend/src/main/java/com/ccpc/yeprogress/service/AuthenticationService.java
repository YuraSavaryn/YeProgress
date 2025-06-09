package com.ccpc.yeprogress.service;

import com.ccpc.yeprogress.dto.AuthenticationDTO;
import com.ccpc.yeprogress.exception.UserNotFoundException;
import com.ccpc.yeprogress.exception.UserValidationException;
import com.ccpc.yeprogress.logger.LoggerService;
import com.ccpc.yeprogress.mapper.AuthenticationMapper;
import com.ccpc.yeprogress.model.Authentication;
import com.ccpc.yeprogress.model.User;
import com.ccpc.yeprogress.model.types.AuthenticationStatusType;
import com.ccpc.yeprogress.repository.AuthenticationRepository;
import com.ccpc.yeprogress.validation.ValidationService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthenticationService {

    private static final Logger logger = LoggerService.getLogger(AuthenticationService.class);

    private final AuthenticationRepository authenticationRepository;
    private final AuthenticationMapper authenticationMapper;
    private final ValidationService validationService;

    @Autowired
    public AuthenticationService(AuthenticationRepository authenticationRepository,
                                 AuthenticationMapper authenticationMapper,
                                 ValidationService validationService) {
        this.authenticationRepository = authenticationRepository;
        this.authenticationMapper = authenticationMapper;
        this.validationService = validationService;
    }

    @Transactional
    public AuthenticationDTO createAuthentication(AuthenticationDTO authenticationDTO) {
        LoggerService.logCreateAttempt(logger, "Authentication", authenticationDTO.getMethodName());

        try {
            validationService.validateAuthenticationDTO(authenticationDTO);

            Authentication authentication = authenticationMapper.toEntity(authenticationDTO);
            Authentication savedAuthentication = authenticationRepository.save(authentication);
            LoggerService.logCreateSuccess(logger, "Authentication", savedAuthentication.getAuthenticationId());

            return authenticationMapper.toDto(savedAuthentication);

        } catch (UserValidationException e) {
            LoggerService.logError(logger, "Validation failed for authentication creation: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            LoggerService.logUnexpectedError(logger, "authentication creation", e.getMessage(), e);
            throw new RuntimeException("Failed to create authentication due to unexpected error", e);
        }
    }

    @Transactional
    public AuthenticationDTO createAuthentication(User user) {
        LoggerService.logCreateAttempt(logger, "Authentication", user != null ? user.getUserId() : "null");

        try {
            if (user == null || user.getUserId() == null) {
                LoggerService.logValidationFailure(logger, "User is required");
                throw new UserValidationException("User is required for authentication creation");
            }

            Authentication authentication = new Authentication();
            authentication.setUser(user);
            authentication.setStatus(AuthenticationStatusType.NOT_VERIFIED);
            Authentication savedAuthentication = authenticationRepository.save(authentication);
            LoggerService.logCreateSuccess(logger, "Authentication", savedAuthentication.getAuthenticationId());

            return authenticationMapper.toDto(savedAuthentication);

        } catch (UserValidationException e) {
            LoggerService.logError(logger, "Validation failed for authentication creation: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            LoggerService.logUnexpectedError(logger, "authentication creation", e.getMessage(), e);
            throw new RuntimeException("Failed to create authentication due to unexpected error", e);
        }
    }

    public AuthenticationDTO getAuthenticationByUserId(Long userId) {
        LoggerService.logRetrieveAttempt(logger, "Authentication", userId);

        try {
            Authentication authentication = authenticationRepository.findByUser_UserId(userId);
            if (authentication == null) {
                LoggerService.logEntityNotFound(logger, "Authentication", userId);
                throw new UserNotFoundException("Authentication for user ID " + userId + " not found");
            }

            LoggerService.logRetrieveSuccess(logger, "Authentication", userId);
            return authenticationMapper.toDto(authentication);

        } catch (UserNotFoundException e) {
            LoggerService.logError(logger, "Error retrieving authentication: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            LoggerService.logUnexpectedError(logger, "authentication retrieval", e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve authentication due to unexpected error", e);
        }
    }

    public List<AuthenticationDTO> getAllAuthentications() {
        LoggerService.logRetrieveAttempt(logger, "All Authentications", "all");

        try {
            List<Authentication> authentications = authenticationRepository.findAll();
            LoggerService.logRetrieveSuccess(logger, "All Authentications", authentications.size());

            return authentications.stream()
                    .map(authenticationMapper::toDto)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            LoggerService.logUnexpectedError(logger, "retrieval of all authentications", e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve authentications due to unexpected error", e);
        }
    }

    @Transactional
    public AuthenticationDTO updateAuthentication(Long userId, AuthenticationDTO authenticationDTO) {
        LoggerService.logUpdateAttempt(logger, "Authentication", userId);

        try {
            validationService.validateAuthenticationDTO(authenticationDTO);

            Authentication authentication = authenticationRepository.findByUser_UserId(userId);
            if (authentication == null) {
                LoggerService.logEntityNotFound(logger, "Authentication", userId);
                throw new UserNotFoundException("Authentication for user ID " + userId + " not found");
            }

            authenticationMapper.updateEntityFromDto(authenticationDTO, authentication);
            Authentication savedAuthentication = authenticationRepository.save(authentication);
            LoggerService.logUpdateSuccess(logger, "Authentication", userId);

            return authenticationMapper.toDto(savedAuthentication);

        } catch (UserValidationException | UserNotFoundException e) {
            LoggerService.logError(logger, "Error updating authentication: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            LoggerService.logUnexpectedError(logger, "authentication update", e.getMessage(), e);
            throw new RuntimeException("Failed to update authentication due to unexpected error", e);
        }
    }

    @Transactional
    public void deleteAuthentication(Long id) {
        LoggerService.logDeleteAttempt(logger, "Authentication", id);

        try {
            if (!authenticationRepository.existsById(id)) {
                LoggerService.logEntityNotFound(logger, "Authentication", id);
                throw new UserNotFoundException("Authentication with ID " + id + " not found");
            }

            authenticationRepository.deleteById(id);
            LoggerService.logDeleteSuccess(logger, "Authentication", id);

        } catch (UserNotFoundException e) {
            LoggerService.logError(logger, "Error deleting authentication: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            LoggerService.logUnexpectedError(logger, "authentication deletion", e.getMessage(), e);
            throw new RuntimeException("Failed to delete authentication due to unexpected error", e);
        }
    }
}