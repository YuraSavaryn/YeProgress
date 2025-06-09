package com.ccpc.yeprogress.service;

import com.ccpc.yeprogress.dto.SocialNetworkDTO;
import com.ccpc.yeprogress.exception.UserNotFoundException;
import com.ccpc.yeprogress.exception.UserValidationException;
import com.ccpc.yeprogress.logger.LoggerService;
import com.ccpc.yeprogress.mapper.SocialNetworkMapper;
import com.ccpc.yeprogress.model.SocialNetwork;
import com.ccpc.yeprogress.model.User;
import com.ccpc.yeprogress.repository.SocialNetworkRepository;
import com.ccpc.yeprogress.repository.UserRepository;
import com.ccpc.yeprogress.validation.ValidationService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SocialNetworkService {

    private static final Logger logger = LoggerService.getLogger(SocialNetworkService.class);

    private final SocialNetworkRepository socialNetworkRepository;
    private final UserRepository userRepository;
    private final SocialNetworkMapper socialNetworkMapper;
    private final ValidationService validationService;

    @Autowired
    public SocialNetworkService(SocialNetworkRepository socialNetworkRepository,
                                UserRepository userRepository,
                                SocialNetworkMapper socialNetworkMapper,
                                ValidationService validationService) {
        this.socialNetworkRepository = socialNetworkRepository;
        this.userRepository = userRepository;
        this.socialNetworkMapper = socialNetworkMapper;
        this.validationService = validationService;
    }

    @Transactional
    public SocialNetworkDTO createSocialNetwork(SocialNetworkDTO socialNetworkDTO) {
        LoggerService.logCreateAttempt(logger, "SocialNetwork", socialNetworkDTO.getUserId());

        try {
            validationService.validateSocialNetworkDTO(socialNetworkDTO);

            User user = userRepository.findById(socialNetworkDTO.getUserId())
                    .orElseThrow(() -> {
                        LoggerService.logEntityNotFound(logger, "User", socialNetworkDTO.getUserId());
                        return new UserNotFoundException("User with ID " + socialNetworkDTO.getUserId() + " not found");
                    });

            SocialNetwork socialNetwork = socialNetworkMapper.toEntity(socialNetworkDTO);
            socialNetwork.setUser(user);
            SocialNetwork savedSocialNetwork = socialNetworkRepository.save(socialNetwork);
            LoggerService.logCreateSuccess(logger, "SocialNetwork", savedSocialNetwork.getContactId());

            return socialNetworkMapper.toDto(savedSocialNetwork);

        } catch (UserValidationException | UserNotFoundException e) {
            LoggerService.logError(logger, "Error creating social network: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            LoggerService.logUnexpectedError(logger, "social network creation", e.getMessage(), e);
            throw new RuntimeException("Failed to create social network due to unexpected error", e);
        }
    }

    public SocialNetworkDTO getSocialNetworkById(Long id) {
        LoggerService.logRetrieveAttempt(logger, "SocialNetwork", id);

        try {
            SocialNetwork socialNetwork = socialNetworkRepository.findById(id)
                    .orElseThrow(() -> {
                        LoggerService.logEntityNotFound(logger, "SocialNetwork", id);
                        return new UserNotFoundException("Social network with ID " + id + " not found");
                    });

            LoggerService.logRetrieveSuccess(logger, "SocialNetwork", id);
            return socialNetworkMapper.toDto(socialNetwork);

        } catch (UserNotFoundException e) {
            LoggerService.logError(logger, "Error retrieving social network: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            LoggerService.logUnexpectedError(logger, "social network retrieval", e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve social network due to unexpected error", e);
        }
    }

    public List<SocialNetworkDTO> getAllSocialNetworks() {
        LoggerService.logRetrieveAttempt(logger, "All SocialNetworks", "all");

        try {
            List<SocialNetwork> socialNetworks = socialNetworkRepository.findAll();
            LoggerService.logRetrieveSuccess(logger, "All SocialNetworks", socialNetworks.size());

            return socialNetworks.stream()
                    .map(socialNetworkMapper::toDto)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            LoggerService.logUnexpectedError(logger, "retrieval of all social networks", e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve social networks due to unexpected error", e);
        }
    }

    @Transactional
    public SocialNetworkDTO updateSocialNetwork(Long id, SocialNetworkDTO socialNetworkDTO) {
        LoggerService.logUpdateAttempt(logger, "SocialNetwork", id);

        try {
            validationService.validateSocialNetworkDTO(socialNetworkDTO);

            SocialNetwork socialNetwork = socialNetworkRepository.findById(id)
                    .orElseThrow(() -> {
                        LoggerService.logEntityNotFound(logger, "SocialNetwork", id);
                        return new UserNotFoundException("Social network with ID " + id + " not found");
                    });

            User user = userRepository.findById(socialNetworkDTO.getUserId())
                    .orElseThrow(() -> {
                        LoggerService.logEntityNotFound(logger, "User", socialNetworkDTO.getUserId());
                        return new UserNotFoundException("User with ID " + socialNetworkDTO.getUserId() + " not found");
                    });

            socialNetworkMapper.updateEntityFromDto(socialNetworkDTO, socialNetwork);
            socialNetwork.setUser(user);
            SocialNetwork savedSocialNetwork = socialNetworkRepository.save(socialNetwork);
            LoggerService.logUpdateSuccess(logger, "SocialNetwork", id);

            return socialNetworkMapper.toDto(savedSocialNetwork);

        } catch (UserValidationException | UserNotFoundException e) {
            LoggerService.logError(logger, "Error updating social network: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            LoggerService.logUnexpectedError(logger, "social network update", e.getMessage(), e);
            throw new RuntimeException("Failed to update social network due to unexpected error", e);
        }
    }

    @Transactional
    public void deleteSocialNetwork(Long id) {
        LoggerService.logDeleteAttempt(logger, "SocialNetwork", id);

        try {
            if (!socialNetworkRepository.existsById(id)) {
                LoggerService.logEntityNotFound(logger, "SocialNetwork", id);
                throw new UserNotFoundException("Social network with ID " + id + " not found");
            }

            socialNetworkRepository.deleteById(id);
            LoggerService.logDeleteSuccess(logger, "SocialNetwork", id);

        } catch (UserNotFoundException e) {
            LoggerService.logError(logger, "Error deleting social network: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            LoggerService.logUnexpectedError(logger, "social network deletion", e.getMessage(), e);
            throw new RuntimeException("Failed to delete social network due to unexpected error", e);
        }
    }
}